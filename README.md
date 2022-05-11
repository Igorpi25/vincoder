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

