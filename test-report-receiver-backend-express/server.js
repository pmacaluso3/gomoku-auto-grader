const express = require('express')
const app = express()
app.use(express.json())
const port = 3001

app.get('/', (req, res) => {
  res.send('Hello World!')
})

app.post("/api/test_case_outcomes", (req, res) => {
  console.log(req.body)    
  res.send("ok")
})

app.listen(port, () => {
  console.log(`Example app listening on port ${port}`)
})