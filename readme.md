# k1ui

Suite of tools to programmatically manipulate the screen

Specific goals:
- Selects area on screen, then respond to api requests and actually do the actions
- Capture screen sections quickly, and dump it to some place
- Can send requests like:
  - GET /select: prompts the user to select the desired area on screen
  - GET /mouse/x/y: move mouse to xy location in the selected area
  - GET /global/mouse/x/y: move mouse to xy location globally
  - GET /screen: grab selected area as jpg file
  - GET /screen/x/y/w/h: grab section of selected area as jpg file
  - GET /wheel/delta: scroll mouse wheel by some amount
  - GET /type/abc: types "abc"
  - GET /record: records every action and send the procedures back



