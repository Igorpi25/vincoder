# Что делает приложение?
Есть открытое API https://vpic.nhtsa.dot.gov/api/, где задекларированы VIN-коды автомобилей по производителям и моделям. 
Унифицированная база данных, для централизованного каталога. 
Все производители (Tesla, Aston Martin и т.д) выкладывают производимые модели(Tesla: Model X, Roadster, Model S) по системе VIN-кодов, 
а также составные компоненты к каждой модели.

## Что такое VIN-code?
![VIN-photo](https://github.com/igorpi25/vincoder/blob/media/explainer.png)

Идентификатор автомобиля штампованный на кузове. Что-то вроде паспорта с уникальным номером, который выдают при рождении автомобиля на заводе.
Если отбросить лишние детали, то состоит из следующих частей:

`{страна}{марка}{модель}{год}{серийный_номер}`

https://en.wikipedia.org/wiki/Vehicle_identification_number

## Библиотеки
- [**Paging-3**](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) - постранично загрузка
- [**Room**](https://developer.android.com/training/data-storage/room) - Для кэширования в SQLite
- [**Retrofit**](https://square.github.io/retrofit/) - а также для чтения JSON с null-safety [**Moshi**](https://github.com/square/moshi).
- [**Toothpick**](https://github.com/stephanenicolas/toothpick) - легковесный Dependency Injection
- [**ViewBinding**](https://developer.android.com/topic/libraries/view-binding) - для приведения layout внутри адаптеров

## API
- [`vehicles/getallmanufacturers`](https://vpic.nhtsa.dot.gov/api/vehicles/getallmanufacturers?format=json) - все производители
- [`vehicles/getmakeformanufacturer/{manufacturerId}`](https://vpic.nhtsa.dot.gov/api/vehicles/getmakeformanufacturer/955?format=json) - список изделий опроизводителя
- [`vehicles/getmodelsformakeid/{makeId}`](https://vpic.nhtsa.dot.gov/api/vehicles/getmodelsformakeid/441?format=json) - список моделей для изделия

#### Пример:

[`vehicles/getallmanufacturers`](https://vpic.nhtsa.dot.gov/api/vehicles/getallmanufacturers?format=json) - список производителей (отсюда будет полезно только `Mfr_ID` производителя):

``` js
{
  "Count": 92,
  "Message": "Response returned successfully",
  "Results": [
    {
      "Mfr_ID": 955,
      "Mfr_Name": "TESLA, INC.",
      "Country": "UNITED STATES (USA)",
    },
    {
      "Mfr_ID": 956,
      "Mfr_CommonName": "Aston Martin",
      "Country": "UNITED KINGDOM (UK)",
    },
    // ...
  ]
}
```
<details>
<summary><a href="https://github.com/igorpi25/vincoder/blob/media/main_1.png">main.png</a></summary>

![VIN-photo](https://github.com/igorpi25/vincoder/blob/media/main_1.png) 

</details>

Далее, получаем список продукции выбранного производителя(мы выбрали первый из них - `TESLA, INC`), по полученному `Mfr_ID`(955)

[`vehicles/getmakeformanufacturer/955`](https://vpic.nhtsa.dot.gov/api/vehicles/getmakeformanufacturer/955?format=json):

``` js
{
  "Count": 1,
  "Message": "Results returned successfully",
  "SearchCriteria": "Manufacturer:955",
  "Results": [
    {
      "Make_ID": 441,
      "Make_Name": "TESLA",
      "Mfr_Name": "TESLA, INC."
    }
  ]
}
```

И наконец, передав `Make_ID`(441) в следующий запрос, получаем список моделей:

[`vehicles/getmodelsformakeid/441`](https://vpic.nhtsa.dot.gov/api/vehicles/getmodelsformakeid/441?format=json)
``` js
{
  "Count": 5,
  "Message": "Response returned successfully",
  "SearchCriteria": "Make:441",
  "Results": [
    {
      "Make_ID": 441,
      "Make_Name": "TESLA",
      "Model_ID": 1685,
      "Model_Name": "Model S"
    },
    {
      "Make_ID": 441,
      "Make_Name": "TESLA",
      "Model_ID": 2071,
      "Model_Name": "Roadster"
    },
    {
      "Make_ID": 441,
      "Make_Name": "TESLA",
      "Model_ID": 10199,
      "Model_Name": "Model X"
    },
    // ...
  ]
}
```
<details>
<summary><a href="https://github.com/igorpi25/vincoder/blob/media/tesla.png">tesla.png(441)</a></summary>

![VIN-photo](https://github.com/igorpi25/vincoder/blob/media/tesla.png) 

</details>

## Манифест
В данной демке, автор не ставит целью строгое соблюдение рекомендации **Clean Architecture** и других архитектурных подходов.
Целью является разложение всех задействованных компонентов отдельно взятой библиотеки [**Paging-3**](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) на составные части. 
Определение функций отдельно взятых частей, объяснение их природы (генезис), выявление связей. 

>Перед вами личный конспект, заметки и наблюдения, которые я посчитал важным для понимания предмета

## Компоненты из Paging-3

### Pager
> TO-DO Сущность живущая на UI слое. 

### PagingData
Это конечные результаты, которые потребляет PagingAdapter. Приходят в виде эмиссий `Pager.flow`

### PagingState
Snapshot-снимок текущего состояния используемый в качестве уникального ключа(индекса) в `PagingSource`, для того чтобы получить следующий номер страницы из API. 
Также применяется в других частях в качестве уникального ключа(индекса)
Обратите внимание на перегрузку опреаторов `equals`, `hashCode`, `toString`, что еще раз указывает на его назначение

Как только будет скачана следующая порция из API, то `PagingState` должен заново перегенерироваться для нового состояния `PagingData`. Используется внутри `PageFetcherSnapshotState`. 

В документации часто упоминается, что `PagingState` и `PagingData` - это пара. Мы будем представлять их как пару "ключ-значение".

### Mediator
> TO-DO Из Ui мы дергаем эту штуку. Он в свою очередь дергает repository(gateway)

Основной "опыт" идет тут. Он работает как с удаленной API так и с локальной БД. 
Состоит из следующих сущностей: 

**`LoadType`** - этот _sealed class_, который является **доменной моделью библиотеки Paging**, ограничивающий степень свободы всей Paging библиотеке. Благодаря ему у нас есть всего одна операция: `load`, с разновидностью: `REFRESH`, `APPEND`, `PREPEND`. Разновидность задается в виде _sealed class_ параметра к операции, что опять таки устанавливает ограничения. Сразу видно, что торчат ноги Clean Architecture. Видимо, таким образом они зафисксировали некий контракт, для того чтобы опираться на него в смежных компонентах.

Теоритически, мы могли бы добавить новую операцию, например `TIE`, но из-за _sealed class_ мы не сможем его расширить, кроме того оно прямо указывает что не надо так делать. Изучив исходники, автор убедился, что все компоненты завязаны под контракт этого домена(`LoadType`), и ничего не поделать. Итого мы имеем примитивную общепринятую библиотеку, который задает тон всей индустрии. Автор предвидит появление тысячи собственных решений от каждой компании, вместо одного стандарта (зачем нужен JetPack тогда? Опять это был не стандарт, а рекомендации?)

### PagingSource
> Сомневаюсь, что понятие "постраничность"(Paging) должно сущнествовать на уровне _Repository_. Термин из мира UI перенесли на другой слой. По-хорошему, репозиторий не знает ничего о "страницах", ему все равно. Для него cуществуют только запросы, с разными параметрами

Эта штука задумана для того чтобы абстрагировать работу с _Retrofit_ или с _локальной БД_, и предоставить `Meadiator` концы которые он дергает. Сигнатуры к этому прямо призывают, все построено вокруг тех же: `REFRESH`, `APPEND`, `PREPEND`

По странным причинам, флаг `enablePlaceholder` тянется бойлерплейтом сквозь все компоненты. Видимо, был неудачный официальный пример, из-за которого пришлось идти на компромис. Возможно, таковое поддерживается на уровне API бэкенда, в выбранном официальном примере, и ничего лучше не придумали как тянуть его сквозь архитектуру 

Не используется. Все реализовано хардкодом внутри `Mediator`. Там же вызов и ожидание _Retrofit_ там же работа с _Room_

`PagingSource` - может только расти(grow), за счет скачивания новых страниц, но не может изменить уже существующие(ранее скачанные) элементы. 

Если необходимо изменить существующий элемент, то `PagingSource` нужно пересоздать заново. Работает в тесном контакте с `PagingData`, также нужно пересоздать в этом случае.

Автор видит причину такой SOLID (open/close) иммутабельности элементов, в устройстве `PagingState`. Который служит в качестве индекса.

**Объяснение иммутабельности PagingState**

Данную архитектуру, ваш покорный слуга, объясняет следущим образом. Paging библиотека нужна для того чтобы по частям скачать некий большой иммутабельный объект из сервера. Этот объект является застывшим во времени в контектсе Paging библиотеки. Paging имеет другое время, которое течет "перпендикулярно" течению времени сервера. Paging как мелкое насекомое для масштаба сервера. И Paging не может осознать и восприминимать изменение сервера, этот процесс для его реальности не существует. 

Когда сервер меняется. Paging умирает и зарождается новое поколение. По факту пересоздается `PagingSource`, `PagingData`. Идет перерождения репозитория Paging. 

Paging создает по кускам проекцию застывшего образа сервера у себя на локальной БД. Это его жизнь, и его жизненный путь. 

Изменение сервера, в этом случае должно отслеживаться через другой механизм. Через мой классический механизм дельта изменений. Для "дельта-изменений" в свою очередь локальная проекция Paging является застывшей картинкой, которую он меняет. Направление течения времени для "дельта-изменений" совпадает с направлением и скоростью изменений сервера.

По хорошему, система "дельта-изменений" легко может посчитать застывший образ Paging как часть проекции, которую также нужно изменить. В этом случае отпадает необходимость перерождения Paging при изменении сервера

### PagingConfig
Настройки определяющие характер работы Pager. 

Судя по исходникам, этот же элемент "подразумевает" аналогичное поведение для `PagingSource`. Именно подразумевает, а не устанавливает. Если PagingSource будет выдавать иначе, чем это отрисовывается на Ui с помощью Pager, то все сломается. Таким образом, можно констатировать отсутствие гибкости между слоями _UI, Domain и Data_. Какими порциями все приходит из внешнего API, придется так его и получать из БД тоже. 

Из этого можно делать вывод, что `PageData`, `PageEvent` также имеют характер доменных сущностей. Мы ограничены не только в видах операций(`REFRESH`, `APPEND`, `PREPEND`), но и размерах(_boundaries_) получаемых "порций". Разные источники данных, не могут выдавать разные порции данных, т.к. `Pager` не сможет их "отобразить" без костылей.

# Компоненты приложения

## CommonPagingAdapter

Автор использует один и тот-же адаптер `CommonPagingAdapter` для всех фрагментов. Таким образом автор избегает создания нового адаптера для каждого фрагмента. Общий адаптер лежит в папке `ui/common`

Отличается только набор используемых `ViewHolder`, в зависимости от получаемых в каждом из экранов данных. 
Данными выступают подклассы `UiModel`(смотрите ниже).

Итак, на входе в адаптер, мы имеем список элементов, которые являются подклассами `UiModel`. 
Для каждого элемента нам нужно создать `ViewHolder` нужного типа.

Тип `ViewHolder` определяем используя оператор `when` к [sealed](https://kotlinlang.org/docs/sealed-classes.html)-классу `UiModel`

```kt
override fun getItemViewType(position: Int): Int {
    return when(getItem(position)){
        is UiModel.ManufacturerItem -> TYPE_MANUFACTURER
        is UiModel.ModelItem -> TYPE_MODEL
        is UiModel.PageItem -> TYPE_PAGE
        is UiModel.MakeItem -> TYPE_MAKE
        null -> throw UnsupportedOperationException("Unknown view position="+position)
    }
}
```

Далее зная тип создаем/биндим нужный `ViewHolder`.

## UiModel
Единый формат моделей `ViewHolder`, которые используются в `CommonPagingAdapter`.

``` kotlin
sealed class UiModel {
    data class ManufacturerItem(val manufacturer: Manufacturer): UiModel()
    data class ModelItem(val model: Model): UiModel()
    data class PageItem(val page: Int): UiModel()
    data class MakeItem(val name: String): UiModel()
}
```

Если на доменном слое у нас используются модели: 
- `Manufacturer`
- `Make`
- `Model`

В UI слое мы используем следующие модели:
- ManufacturerItem
- MakeItem
- ModelItem
- PageItem (номера страниц в RecyclerView, чтобы наглядно демонстрировать работу Paging)

> Сейчас UI-модели по сути wrapper над доменными моделями. Но в реальном проекте, эти модели будут лежать на отдельном пакете(модуле) вместе с UI

## Converters(Mapping)

Общая суть такая. Берет объекты уровня Domain и переделывает в объекты уровня UI:
* Берет `Manufacturer` и превращает в `UiModel`
* Берет `Model` и превращает в `UiModel`

> Речь идет об `Flow<PagingData<Manufacturer>>` и `Flow<PagingData<Model>>`, которые превращаются в `Flow<PagingData<UiModel>>`

Есть два конвертера: `MainConverter`, `DetailsConverter`. 

**MainConverter** 

Между элементами, вставляются номера страниц `UiModel.PageItem` в `MainConverter`. Для этого сравниваем каждый `manufacturer.page` последующего элемента с предыдущим, если номера у них разные то между этими двумя `UiModel.ManufacturerItem` вставляем `UiModel.PageItem`.

**DetailsConverter**

Исходные данные: Список моделей, получаемых из БД, сгруппированные по изделиям.
Задача такая: Сверху(на шапке) информация о производителе. Затем информация об первом изделии, следом список его моделей. Далее информация об втором изделии и его список моделей. И так для всех изделий.

Для этого:
1. Идем по списку моделей, и между ними вставляем информацию об изделиях. По аналогии с `MainConverter`
2. Вставляем в начале списка информацию о производителе

> Конвертеры являются сущностями UI слоя, они по-сути придатки к логике представления экранов. Если быть точнее, то представления данных внутри `RecyclerView`.

## Бизнес логика

> На данное время, автор захардкодил эти части кода, т.к. в момент написания приложения автор не был знаком с данной библиотекой, именно благодаря этой  демке компоненты заняли свои места в боевом проекте

### ModelRemoteMediator

Много неприятного кода. Начнем снимать слои, один за другим. 

**Первый слой.** Состоит всего из двух вариантов, которые может вернуть функция: `MediatorResult.Success` или `MediatorResult.Error`.

``` kt
override suspend fun load(loadType: LoadType, state: PagingState<Int, Model>): MediatorResult {
        return try {
        
           MediatorResult.Success(...)
           
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
```

**Второй слой.** Состояние _MediatorResult.Success_ может быть двух варантов, определяется параметром `endOfPagination`: 
- `endOfPagination = true` значит достигли конца, следующей страницы не будет. Поэтому такие варианты сразу ведут к **_return_** функции
- `endOfPagination = false` будет как минимум еще одна страница. Дальнейшая работа сводится к тому, чтобы найти **`nextMakeId`** следующей страницы

`MediatorResult.Success` с параметром `endOfPagination` - это то, что мы возвращаем в контексте `load` функции. На данном слое, нет сомнений в том что мы вернем `Success`. Разница может быть лишь в том, является ли страница последней, т.е. `endOfPagination` вернем со значением `true` или `false`?

**Третий слой.** Флаг `endOfPagination` зависит от типа операции: `REFRESH`, `PREPEND`, `APPEND`. Внутри конструкции `when` мы можем либо прервать выполнение  `load`, либо обяза найти следующий `nextMakeId`

``` kt
val nextMakeId : Int = when (loadType) {
                LoadType.REFRESH -> {
                    // Загружаем первую страницу
                }
                LoadType.PREPEND -> return MediatorResult.Success(
                    // Приложение устроено так, что этот вариант никогда не наступает
                    endOfPaginationReached = true
                )
                LoadType.APPEND -> {
                    // Штатный случай прокрутки вниз, и загрузки следующей страницы
                }
            }
```

В целом, логика вызовов такая. Когда медиатор запускает `load`, нам нужно подгрузить список `Model`-ей для следующего `Make`. Вопрос в том, откуда мы возьмем `nextMakeId` следующего `Make`? Механика медиатора нам его дать не может, мы должны сами об этом подумать

**Проблема получения следующего Make**

Проблема в структуре API запросов. Мы получаем список всех изделий(`Make`) одномоментно. А список моделей(`Model`) изделий мы получаем по отдельности. При этом порядок элементов, как таковой отсутствует в ответе(response) API.
API не поддерживает механизм Paging для закачки моделей. Значит нужно реализовать у себя локально в БД.

**Решение проблемы**

Автор думал над этой проблемой, и на данном этапе решил создать такую схему. Каждый объект `Make` содержит `id`,  следущего за ним объекта. Таким образом в локальной БД объекты `Make`, в совокупности можно представить как цепочку, следующих друг за другом объектов.

Цепочка создается сразу после скачивания перед сохранением списка в БД:
```kt
//Скачанный список
val makes = response.results

//Модифицируем список. В каждый элемент списка записываем `id` следующего за ним элемента
makes.zipWithNext{a, b ->  a.apply { nextId = b.id }}

//Сохраняем список
database.withTransaction {
    makeDao.insertAll(/* makes */) 
}
```

Теперь имея `Make` мы можем получить следующий за ним `nextMakeID`.

> TO-DO В демке, мы не так просто получаем `Make`, как может показаться(и, возможно, как следовало бы). `PagingState` хранит внутри себя список моделей (`Model`). Обратите внимание, что список хранит именно модели а не изделия. Хотя было бы естественно держать список изделий, внутри которых есть список моделей. Вместо этого, `PagingState` даже не подозревает про существование `Make`. Нам приходится получать `makeId` из объектов `Model`, затем делать запрос в БД, чтобы вытащить соотсветствующий `Make`, чтобы затем использовать его `nextMakeId`. Не красиво получается. И совсем не надежно. Плохо пахнет однозначно. Вытаскивать `makeId` из модели это хак. А лишний запрос к БД приводит ненужной асинхронности на пустом месте, где можно было бы избежать этого, тем самым создавая лишний риск `NullPointerException`

**Четвертый слой. Кейс LoadType.REFRESH**

Этот вызов у нас выполняет две задачи:
1. Загрузить список изделий `Make` для выбранного `Manufacturer`
2. Найти `nextMakeId` (в данной кейсе это `firstMakeId`)

```kt
val firstMake = makeDao.getFirstMakeOfManufacturer(targetManufacturer)

if (firstMake == null ) {
    // Значит изделия и модели для выбранного `Manufacturer` еще ни разу не кэшировались в БД. Следовательно нужно скачать список изделий из API
    val response = retrofitService.getMakesOfManufacturer(manufacturerId = targetManufacturer).await()
    
    // Что делаем со списком изделий?
    if (response.results.isNotEmpty()) {
        // Есть список изделий. Придаем списку свойства цепочки, как было описано выше
        val makes = response.results
        makes.zipWithNext{a, b ->  a.apply { nextId = b.id }}
        database.withTransaction {
            // Этот кусок рассмотрим далее
            makeDao.insertAll(*makes.plusElement(response.results.last()).map { it.apply { it.manufacturerId = targetManufacturer } }.toTypedArray())
        }
        
        // Будем качать модели для первого изделия
        makes.first().id
    } else {
        // У производителя нет изделий. Значит нечего показать, и мы сразу можем посчитать, что достигли конца списка.
        return MediatorResult.Success(
            endOfPaginationReached = true
        )
    }

} else {
    firstMake.id
}
```

Расмотрим этот кусок подробнее:
```kt
makeDao.insertAll(*
    // В результате особенностей работы `zipWithNext`, последний элемент списка пропадает. Снова добавляем этот элемент в конец списка. `nextMakeId` для него будет `null`
    makes.plusElement(response.results.last()).
    // Добавляем ко всем изделиям 'manufacturerId'
    map {it.apply { it.manufacturerId = targetManufacturer }}.
    toTypedArray()
)
```


**Четвертый слой. Кейс LoadType.APPEND**

Найти `nextMakeId`, если такой существует. Иначе прервать `load`.

**Пятый слой. Загрузка и сохранение в БД моделей для найденного `nextMakeId`**

Если выполнение `load` дошло до этой точки, значит мы нашли необходимый `nextMakeId`. Cкачиваем модели из API, и сохраняем в БД:
```kt

val response = retrofitService.getModelsForMakeId(makeId = nextMakeId).await()

response.results.
  takeIf { it.isNotEmpty() }?.
  // Если список не пустой. Записываем модели в БД
  let {
    database.withTransaction {
        // Добавляем `manufacturerId` к моделям
        modelDao.insertAll(*it.map { it.apply { it.manufacturerId = targetManufacturer } }.toTypedArray())
    }
  }

// Завершаем `load` с соответсвующим `endOfPaginationReached`
MediatorResult.Success(
    endOfPaginationReached = response.results.size == 0
)
```
