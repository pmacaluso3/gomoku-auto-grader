const express = require('express')
const app = express()
const port = 3000

app.get('/', (req, res) => {
  res.send('Hello World!')
})

app.post('/success', (req, res) => {
    console.log("success!")
})

app.post('/failure', (req, res) => {
    console.log("failure!")
})

app.listen(port, () => {
  console.log(`Example app listening on port ${port}`)
})