# simple_ktor_api
Предоставляет данные из CSV файла в виде restfull api

`api/access/data/*` - Классы данных, которые `Access` будет возвращать

`api/access/Access` - интерфейс через который происходит общение модов с источником данными

`api/access/UralsCSVAccess` - Класс предоставляющий данные из CSV файла. extend `Access`

`api/mods/Mod` - Интерфейс описивающий функционал API. И также имеет метод  `doc` возвращающий документацию мода.

`api/mods/*` - Классы функционала, разделенные на отдельные файлы, общаются с `Access`. extend Mod

`api/statistics/StatTrack` - Класс, позицианирует себя как Access отслеживающий использование `Access` который ему передали в конструкторе, также является модом, который возвращает статистику использования `Access`

`Appication` - Точка входа инициализирует `UralsCSVAccess`, обарачивает его в `StatTrack`. Собирает все моды в `HashMap<String, Mod>`, таким образом задает моду название по которому и будет происходить обращение к нему. При обращении к корню `/` возвращает `doc` всех модов. Если мода с запрашиваемым именем не существует, запрос игнорируется. 

Копия доков из программы если обратиться к корню `/`
```
/date - Get value for given date
Params
 - date=YYYY-MM-DD - Date

/min_max - Get minimum and maximum over given range, return as Json object { min=.., max=.. }
Returns NaN if no data in range
Params
 - lo - Lower bound YYYY-MM-DD, Inclusive
 - hi - Upper bound YYYY-MM-DD, Exclusive
 - optional: strict - Default: false. Then true will require that data date range in fully in bounds with given range


/stat - Provide api usage statistics
No arguments

/get_avg - Get average over given date range.
Returns NaN if no data in range
Params
 - lo - Lower bound YYYY-MM-DD, Inclusive
 - hi - Upper bound YYYY-MM-DD, Exclusive
 - optional: strict - Default: false. Then true will require that data date range in fully in bounds with given range

```
