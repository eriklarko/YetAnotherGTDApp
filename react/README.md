# Dependencies
* node 
* npm
* git

# Getting started

## Install nodejs
Install from https://nodejs.org/download/
This will install node and npm

## It is assumed that you have git installed
If not - install it. (Also: How did you get this file?)

## Add git to PATH
### On Windows

* Search for `environment` in metro mode
* Append the folder where git.exe is located to the PATH variable. Lasu's was at `C:\Program Files(x86)\git\bin`. Remember to add the semi-colon separator before the folder.

### On Linux
Git is already on the path!

## Install dependencies

All commands below should be executed from *frontend* directory.

### On Windows
Replace {npmpath} with the output from `npm bin`

### On Linux
Replace {npmpath} with $(npm bin)

```
npm install
{npmpath}/jspm install
```

### Transpile (Compile)
```
{npmpath}/ntsc
```
or
```
npm run-script build
```

### Run
The following will start a webserver on port 80. (-c-1 disables all caching) (No sudo on win)
```
sudo {npmpath}/http-server -p80 -c-1
```

### Test
```
npm run-script test
```
