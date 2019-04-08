const express = require('express')
const path = require('path')
const app = express()

app.use(express.static(path.join(__dirname, './src/main/resources')))

app.listen(616, () => {
  console.log(`http://172.17.110.216:616/tab/icon/tab_home_selected.png`)
})