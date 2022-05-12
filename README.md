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
> TO-DO Только что полученный кусок данных

### Mediator
> TO-DO Из Ui мы дергаем эту штуку. Он в свою очередь дергает repository(gateway)

Основной "опыт" идет тут. Он работает как с удаленной API так и с локальной БД. 
Состоит из следующих сущностей: 

**`LoadType`** - этот _sealed class_, который является **доменной моделью библиотеки Paging**, ограничивающий степень свободы всей Paging библиотеке. Благодаря ему у нас есть всего одна операция: `load`, с разновидностью: `REFRESH`, `APPEND`, `PREPEND`. Разновидность задается в виде _sealed class_ параметра к операции, что опять таки устанавливает ограничения. Сразу видно, что торчат ноги Clean Architecture. Видимо, таким образом они зафисксировали некий контракт, для того чтобы опираться на него в смежных компонентах.

Теоритически, мы могли бы добавить новую операцию, например `TIE`, но из-за _sealed class_ мы не сможем его расширить, кроме того оно прямо указывает что не надо так делать. Изучив исходники, автор убедился, что все компоненты завязаны под контракт этого домена(`LoadType`), и ничего не поделать. Итого мы имеем примитивную общепринятую библиотеку, который задает тон всей индустрии. Автор предвидит появление тысячи собственных решений от каждой компании, вместо одного стандарта (зачем нужен JetPack тогда? Опять это был не стандарт, а рекомендации?)

### PagingSource
> TO-DO Сомневаюсь, что понятие "постраничность"(Paging) должно сущнествовать на уровне Repository. Термин из мира UI перенесли на слой Repository. По-хорошему, репозиторий не знает ничего о "страницах", ему все равно. Для него cуществуют запросы с разными параметрами
> Эта штука задумана для того чтобы абстрагировать работу с Retrofit или с локальной БД, и предоставить `Meadiator` концы которые он дергает. Сигнатуры к этому прямо призывают, все построено вокруг тех же: `REFRESH`, `APPEND`, `PREPEND`

### PagingConfig
> TO-DO Настройки определяющие характер работы Pager

## Компоненты приложения

## UiModel
Единый формат моделей для `ViewHolder`, с которым умеет работать общий адаптер. Автор использует один и тот-же адаптер для всех фрагментов. Отличается только типы `ViewHolder`, в зависимости от полученных данных. Данные определеются на слое `ViewModel`
Таким образом автор избегает создание нового адаптера для каждого фрагмента. 

``` kotlin
sealed class UiModel {
    data class ManufacturerItem(val manufacturer: Manufacturer): UiModel()
    data class ModelItem(val model: Model): UiModel()
    data class PageItem(val page: Int): UiModel()
    data class MakeItem(val name: String): UiModel()
}
```

Общий адаптер лежит в папке `ui/common`

## Converter(Mapper)
Берет объекты уровня Domain и переделывает в объекты уровня UI:
* Берет `Manufacturer` и превращает в `UiModel`
* Берет `Model` и превращает в `UiModel`

> Речь идет об `Flow<PagingData<Manufacturer>>` и `Flow<PagingData<Model>>`, которые превращаются в `Flow<PagingData<UiModel>>`

## 

