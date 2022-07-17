# k1ui

Suite of tools to programmatically manipulate the screen. This is a single Java
program that can record mouse and keyboard events, send it over the network and
plays out mouse/keyboard events given to it

Specific goals:
- Selects area on screen, then respond to api requests and actually do the actions
- Capture screen sections quickly, and dump it to some place
- Status endpoints:
  - `GET /test`: test endpoint to make sure the server is running
  - `GET /screen`: grab the selected area as jpg file
  - `GET /record`: records every action and send the events back
  - `GET /events`: grab recent events in the last 1 sec window
- Action endpoints:
  - `GET /select`: prompts the user to select the desired area on screen
  - `POST /apply`: applies a series of mouse and keyboard operations
  - `GET /mouse/:x/:y`: move mouse to xy location in the selected area. Quick method instead of `/apply`
  - `GET /wheel/:delta`: scroll mouse wheel by some amount
  
# Setting up

- Go to the `/releases` and download the zip file
- Unzips the file. It should have the following folder structure:

```shell
(torch) kelvin@mint-2:~/repos/java/k1ui/releases$ tree k1ui-shadow-1.0
k1ui-shadow-1.0
├── bin
│   ├── k1ui
│   └── k1ui.bat
└── lib
    └── k1ui-1.0-all.jar

2 directories, 3 files
```

- For Linux/Mac users, run the shell script at `bin/k1ui`. For Windows users, run the batch script at `bin/k1ui.bat`
- This will start up a GUI application and also a server at `0.0.0.0:9511`
- You can start sending requests to the server so that it does what you want. For example:

```bash
curl localhost:9511/screen # grabs a screenshot of the selected region
```

However, it's suggested that you use the python module [`k1lib.ui`](https://k1lib.com) module instead as it presents a more pleasant
environment to work in.

