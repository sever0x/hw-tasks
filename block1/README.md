# JSON Parser

This is a Java-based console application that parses a directory of JSON files containing music data and generates statistics on the specified attribute.

## Features

- Supports parsing JSON files with music data
- Calculates statistics on various attributes of the music data, such as title, artist, album, release year, genres, and duration
- Allows the user to specify the attribute to generate statistics for
- Supports multi-threaded parsing for improved performance
- Outputs the statistics to an XML file in descending order of the attribute count

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven

### Installing

1. Clone the repository:
```
git clone https://github.com/sever0x/hw-tasks.git
```

2. Navigate to the project directory:
```
cd hw-tasks/block1
```

3. Build the project:
```
mvn clean install
```

### Usage

To run the application, use the following command:

```
java -jar target/block1-1.0-SNAPSHOT.jar <directory_path> <attribute_name>
```

Replace `<directory_path>` with the path to the directory containing the JSON files, and `<attribute_name>` with the attribute to generate statistics for (e.g., "title", "artist", "album", "releaseYear", "genres", "duration").

For example:

```
java -jar target/block1-1.0-SNAPSHOT.jar C:\Users\user\path\tasks\block1\src\main\resources\json genres
```

This will generate an XML file named `statistics_by_genres.xml` containing the statistics for the "genres" attribute.

## Input/Output

### Input

The application expects a directory containing JSON files with the following structure:

```json
[
  {
    "title": "Like a Rolling Stone",
    "artist": {
      "name": "Bob Dylan",
      "country": "US"
    },
    "album": "Highway 61 Revisited",
    "genres": [
      "Rock",
      "Folk Rock"
    ],
    "duration": 378,
    "releaseYear": 1965
  },
  {
    "title": "Purple Haze",
    "artist": {
      "name": "Jimi Hendrix",
      "country": "US"
    },
    "album": "Are You Experienced",
    "genres": [
      "Rock",
      "Psychedelic Rock"
    ],
    "duration": 167,
    "releaseYear": 1967
  },
  {
    "title": "Comfortably Numb",
    "artist": {
      "name": "Pink Floyd",
      "country": "Britain"
    },
    "album": "The Wall",
    "genres": [
      "Progressive Rock",
      "Art Rock"
    ],
    "duration": 383,
    "releaseYear": 1979
  }
]
```

### Output

The application will generate an XML file named `statistics_by_{attribute}.xml` in the current working directory, where `{attribute}` is the name of the attribute specified in the command-line arguments. The XML file will contain the statistics for the specified attribute, sorted in descending order by the count.

For example, the contents of `statistics_by_genres.xml` might look like this:

```xml
<statistics>
  <item>
    <value>Rock</value>
    <count>3</count>
  </item>
  <item>
    <value>Folk Rock</value>
    <count>1</count>
  </item>
  <item>
    <value>Psychedelic Rock</value>
    <count>1</count>
  </item>
  <item>
    <value>Progressive Rock</value>
    <count>1</count>
  </item>
  <item>
    <value>Art Rock</value>
    <count>1</count>
  </item>
</statistics>
```

## Performance

The application supports multi-threaded parsing to improve performance. The results of the performance experiments with different numbers of threads are as follows:

- Execution time with 1 thread: 144ms
- Execution time with 2 threads: 7ms
- Execution time with 4 threads: 11ms
- Execution time with 8 threads: 7ms

As you can see, using multiple threads significantly reduces the execution time, especially when using 2 or 8 threads.