## About
This repository contains code for a simple [Monopoly](https://en.wikipedia.org/wiki/Monopoly_(game\)) server. The server's written in [Kotlin](https://kotlinlang.org/) using the [vert.x](https://vertx.io/) framework.

## What's missing?
- a sane looking frontend
- a (Go to jail)/Jail tile
- the free parking tile
- the chance/Community Chest cards and tiles
- train stations
- multiple rooms support (not as an big of an issues as it sounds)
- some win condition; the game will go on as long as their is someone who is not bankrupt

## Building
```bash
git clone https://github.com/jacadzaca/monopoly.git && cd monopoly && ./gradlew shadowJar
```

## Running
```bash
java -jar build/libs/monopoly-1.0.0-SNAPSHOT.jar run com.jacadzaca.monopoly.HttpServer --launcher-class=io.vertx.core.Launcher -Dstarting_balance=2000
```

## The 'Protocol'
See [GameStateController.js](https://github.com/jacadzaca/monopoly/blob/master/resources/GameStateController.js) for an example implementation and [Delta.kt](https://github.com/jacadzaca/monopoly/blob/master/src/main/kotlin/com/jacadzaca/monopoly/gamelogic/Delta.kt) for the 'protocol definition'

## The program's flow
First there is a request that's intercepted by the [Web socket server](https://github.com/jacadzaca/monopoly/blob/master/src/main/kotlin/com/jacadzaca/monopoly/WebSocketGameServer.kt), validate by one of the [validators](https://github.com/jacadzaca/monopoly/tree/master/src/main/kotlin/com/jacadzaca/monopoly/requests/validators) and then either converted into an error message or an [command](https://github.com/jacadzaca/monopoly/tree/master/src/main/kotlin/com/jacadzaca/monopoly/gamelogic/commands).
The command is then executed and a [change](https://github.com/jacadzaca/monopoly/blob/master/src/main/kotlin/com/jacadzaca/monopoly/gamelogic/Delta.kt) [is propagated](https://github.com/jacadzaca/monopoly/blob/master/src/main/kotlin/com/jacadzaca/monopoly/gameroom/GameRoomVerticle.kt).

## Disclaimer
This project is in no way affiliated with, authorized, maintained or endorsed by Hasbro or any of its affiliates or subsidiaries. This is an independent and unofficial project. Use at your own risk.

The code is licensed under an GPL license. Refer to the [LICENSE](https://raw.githubusercontent.com/jacadzaca/monopoly/master/LICENSE) file for more information.

