const express = require('express')
const path = require('path')
const app = express()

app.use(express.static(path.join(__dirname, './src/main/resources')))

app.listen(616, () => {
  console.log(`http://127.0.0.1:616/tab/icon/tab_home_selected.png`)
})