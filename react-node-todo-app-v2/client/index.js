
const root = document.getElementById('root');
root.innerHTML = `
    <h1>Login</h1>
    <input id="username" placeholder="Username" />
    <input id="password" placeholder="Password" type="password" />
    <button onclick="login()">Login</button>
    <div id="error"></div>
`;

function login() {
    fetch('http://localhost:5000/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            username: document.getElementById('username').value,
            password: document.getElementById('password').value
        })
    })
    .then(res => res.ok ? res.json() : Promise.reject(res))
    .then(data => {
        document.body.innerHTML = `
            <h2>Welcome!</h2>
            <input id="new-todo" placeholder="New todo" />
            <button onclick="addTodo()">Add</button>
            <ul id="todo-list"></ul>
        `;
        loadTodos();
    })
    .catch(() => {
        document.getElementById('error').innerText = 'Invalid credentials';
    });
}

function loadTodos() {
    fetch('http://localhost:5000/todos')
    .then(res => res.json())
    .then(todos => {
        const list = document.getElementById('todo-list');
        list.innerHTML = '';
        todos.forEach(todo => {
            const item = document.createElement('li');
            item.innerHTML = `
                <input id="edit-${todo.id}" value="${todo.title}" />
                <button onclick="updateTodo(${todo.id})">Save</button>
                <button onclick="deleteTodo(${todo.id})">Delete</button>
            `;
            list.appendChild(item);
        });
    });
}

function addTodo() {
    const title = document.getElementById('new-todo').value;
    fetch('http://localhost:5000/todos', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ title })
    }).then(loadTodos);
}

function updateTodo(id) {
    const newTitle = document.getElementById(`edit-${id}`).value;
    fetch(`http://localhost:5000/todos/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ title: newTitle })
    }).then(loadTodos);
}

function deleteTodo(id) {
    fetch(`http://localhost:5000/todos/${id}`, {
        method: 'DELETE'
    }).then(loadTodos);
}
