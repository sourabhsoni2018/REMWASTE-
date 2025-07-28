
const express = require('express');
const cors = require('cors');
const app = express();

let todos = [{ id: 1, title: "Sample Todo" }];
let currentId = 2;

app.use(cors());
app.use(express.json());

app.post('/login', (req, res) => {
    const { username, password } = req.body;
    if (username === 'admin' && password === 'password') {
        res.json({ token: '12345' });
    } else {
        res.status(401).json({ error: 'Invalid credentials' });
    }
});

app.get('/todos', (req, res) => res.json(todos));

app.post('/todos', (req, res) => {
    const newTodo = { id: currentId++, title: req.body.title };
    todos.push(newTodo);
    res.status(201).json(newTodo);
});

app.put('/todos/:id', (req, res) => {
    const id = parseInt(req.params.id);
    const todo = todos.find(t => t.id === id);
    if (todo) {
     
        todo.title = req.body.title;
        res.json(todo);
    } else {
        res.status(404).json({ error: 'Not found' });
    }
});

app.delete('/todos/:id', (req, res) => {
    const id = parseInt(req.params.id);
    todos = todos.filter(t => t.id !== id);
    res.json({ success: true });
});

module.exports = app;
