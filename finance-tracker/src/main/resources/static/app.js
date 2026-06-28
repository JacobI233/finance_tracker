const API = 'http://localhost:8080/api';

const getToken = () => localStorage.getItem('token');

const authHeaders = () => ({
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${getToken()}`
});

function showTab(tab) {
    document.getElementById('login-tab').style.display = tab === 'login' ? 'block' : 'none';
    document.getElementById('register-tab').style.display = tab === 'register' ? 'block' : 'none';
    document.querySelectorAll('.tab-btn').forEach((btn, i) => {
        btn.classList.toggle('active', (tab === 'login' && i === 0) || (tab === 'register' && i === 1));
    });
}

async function register() {
    const name = document.getElementById('reg-name').value;
    const email = document.getElementById('reg-email').value;
    const password = document.getElementById('reg-password').value;
    try {
        const res = await fetch(`${API}/auth/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name, email, password })
        });
        if (res.ok) {
            document.getElementById('register-error').textContent = 'Registered! Please login.';
            showTab('login');
        } else {
            document.getElementById('register-error').textContent = 'Registration failed.';
        }
    } catch (e) {
        document.getElementById('register-error').textContent = 'Error connecting to server.';
    }
}

async function login() {
    const email = document.getElementById('login-email').value;
    const password = document.getElementById('login-password').value;
    try {
        const res = await fetch(`${API}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });
        if (res.ok) {
            const token = await res.text();
            localStorage.setItem('token', token);
            window.location.href = 'dashboard.html';
        } else {
            document.getElementById('login-error').textContent = 'Invalid email or password.';
        }
    } catch (e) {
        document.getElementById('login-error').textContent = 'Error connecting to server.';
    }
}

function logout() {
    localStorage.removeItem('token');
    window.location.href = 'index.html';
}

function openModal(id) {
    document.getElementById(id).style.display = 'flex';
    if (id === 'transaction-modal') loadCategoriesIntoSelect('t-category');
    if (id === 'budget-modal') loadCategoriesIntoSelect('b-category');
}

function closeModal(id) {
    document.getElementById(id).style.display = 'none';
}

async function loadCategoriesIntoSelect(selectId) {
    const res = await fetch(`${API}/categories`, { headers: authHeaders() });
    const categories = await res.json();
    const select = document.getElementById(selectId);
    select.innerHTML = '<option value="">Select Category</option>';
    categories.forEach(c => {
        select.innerHTML += `<option value="${c.id}">${c.name} (${c.type})</option>`;
    });
}

async function loadDashboard() {
    if (!getToken()) {
        window.location.href = 'index.html';
        return;
    }
    const now = new Date();
    const month = now.getMonth() + 1;
    const year = now.getFullYear();
    document.getElementById('month-input').value = month;
    document.getElementById('year-input').value = year;
    await loadSummary(month, year);
    await loadTransactions();
    await loadUserName();
    await loadCategories();
}

async function loadSummary(month, year) {
    try {
        const res = await fetch(`${API}/summary?month=${month}&year=${year}`, {
            headers: authHeaders()
        });
        if (res.status === 401) { logout(); return; }
        const data = await res.json();

        document.getElementById('total-income').textContent = `$${data.totalIncome.toFixed(2)}`;
        document.getElementById('total-expenses').textContent = `$${data.totalExpenses.toFixed(2)}`;
        document.getElementById('net-savings').textContent = `$${data.netSavings.toFixed(2)}`;

        const budgetContainer = document.getElementById('budget-list');
        budgetContainer.innerHTML = '';

        if (data.budgetSummaries.length === 0) {
            budgetContainer.innerHTML = '<p style="color:#888;font-size:14px">No budgets set for this month.</p>';
            return;
        }

        data.budgetSummaries.forEach(b => {
            const percent = Math.min((b.actualSpending / b.budgetLimit) * 100, 100);
            budgetContainer.innerHTML += `
                <div class="budget-item">
                    <div class="budget-item-header">
                        <span>${b.categoryName}</span>
                        <div style="display:flex;align-items:center;gap:10px">
                            <span>$${b.actualSpending.toFixed(2)} / $${b.budgetLimit.toFixed(2)}</span>
                            <button class="delete-btn" onclick="deleteBudget(${b.id})">Delete</button>
                        </div>
                    </div>
                    <div class="progress-bar">
                        <div class="progress-fill ${b.overBudget ? 'over' : ''}" style="width:${percent}%"></div>
                    </div>
                    ${b.overBudget ? '<p style="color:#e63946;font-size:12px;margin-top:4px">Over budget!</p>' : ''}
                </div>
            `;
        });
    } catch (e) {
        console.error('Error loading summary', e);
    }
}
async function loadCategories() {
    try {
        const res = await fetch(`${API}/categories`, { headers: authHeaders() 

        });
        const categories = await res.json();
        const container = document.getElementById('category-list');
        container.innerHTML = '';
       
        
        for (const c of categories) {
            container.innerHTML += `
                <div class="category-item">
                    <span>${c.name} (${c.type})</span>
                    <button class="delete-btn" onclick="deleteCategory(${c.id})">Delete</button>
                </div>
            `;
        }
    } catch (e) {
        console.error('Error loading categories', e);
    }
}

async function loadTransactions() {
    try {
        const res = await fetch(`${API}/transactions`, {
            headers: authHeaders()
        });
        const transactions = await res.json();
        const container = document.getElementById('transaction-list');
        container.innerHTML = '';

        if (transactions.length === 0) {
            container.innerHTML = '<p style="color:#888;font-size:14px">No transactions yet.</p>';
            return;
        }

        transactions.forEach(t => {
            const isIncome = t.categoryType === 'INCOME';
            container.innerHTML += `
                <div class="transaction-item">
                    <div>
                        <strong>${t.description || t.categoryName}</strong>
                        <div style="color:#888;font-size:12px">${t.categoryName} • ${t.date}</div>
                    </div>
                    <div style="display:flex;align-items:center;gap:10px">
                        <span class="transaction-amount ${isIncome ? 'income' : 'expense'}">
                            ${isIncome ? '+' : '-'}$${t.amount.toFixed(2)}
                        </span>
                        <button class="delete-btn" onclick="deleteTransaction(${t.id})">Delete</button>
                    </div>
                </div>
            `;
        });
    } catch (e) {
        console.error('Error loading transactions', e);
    }
}
async function loadUserName() {
    try {
        const res = await fetch(`${API}/user`, { headers: authHeaders() });
        if (res.status === 401) { logout(); return; }
        const user = await res.json();
        document.getElementById('user-name').textContent = user.name;
    } catch (e) {
        console.error('Error loading user info', e);
    }
}

async function addTransaction() {
    const categoryId = document.getElementById('t-category').value;
    const amount = document.getElementById('t-amount').value;
    const description = document.getElementById('t-description').value;
    const date = document.getElementById('t-date').value;

    if (!categoryId || !amount || !date) {
        document.getElementById('t-error').textContent = 'Please fill in all required fields.';
        return;
    }

    try {
        const res = await fetch(`${API}/transactions`, {
            method: 'POST',
            headers: authHeaders(),
            body: JSON.stringify({ categoryId: parseInt(categoryId), amount: parseFloat(amount), description, date })
        });
        if (res.ok) {
            closeModal('transaction-modal');
            await loadTransactions();
            const month = document.getElementById('month-input').value;
            const year = document.getElementById('year-input').value;
            await loadSummary(month, year);
        } else {
            document.getElementById('t-error').textContent = 'Failed to add transaction.';
        }
    } catch (e) {
        document.getElementById('t-error').textContent = 'Error connecting to server.';
    }
}
async function addCategory() {
    const name = document.getElementById('c-name').value;
    const type = document.getElementById('c-type').value;

    if (!name || !type) {
        document.getElementById('c-error').textContent = 'Please fill in all required fields.';
        return;
    }

    try {
        const res = await fetch(`${API}/categories`, {
            method: 'POST',
            headers: authHeaders(),
            body: JSON.stringify({ name, type })
        });
        if (res.ok) {
            closeModal('category-modal');
            await loadCategories();
        } else {
            document.getElementById('c-error').textContent = 'Failed to add category.';
        }
    } catch (e) {
        document.getElementById('c-error').textContent = 'Error connecting to server.';
    }
}

async function addBudget() {
    const categoryId = document.getElementById('b-category').value;
    const amountLimit = document.getElementById('b-amount').value;
    const month = document.getElementById('b-month').value;
    const year = document.getElementById('b-year').value;

    if (!categoryId || !amountLimit || !month || !year) {
        document.getElementById('b-error').textContent = 'Please fill in all fields.';
        return;
    }

    try {
        const res = await fetch(`${API}/budgets`, {
            method: 'POST',
            headers: authHeaders(),
            body: JSON.stringify({ categoryId: parseInt(categoryId), amountLimit: parseFloat(amountLimit), month: parseInt(month), year: parseInt(year) })
        });
        if (res.ok) {
            closeModal('budget-modal');
            const m = document.getElementById('month-input').value;
            const y = document.getElementById('year-input').value;
            await loadSummary(m, y);
        } else {
            document.getElementById('b-error').textContent = 'Budget already exists or failed.';
        }
    } catch (e) {
        document.getElementById('b-error').textContent = 'Error connecting to server.';
    }
}

async function deleteTransaction(id) {
    if (!confirm('Delete this transaction?')) return;
    await fetch(`${API}/transactions/${id}`, {
        method: 'DELETE',
        headers: authHeaders()
    });
    await loadTransactions();
    const month = document.getElementById('month-input').value;
    const year = document.getElementById('year-input').value;
    await loadSummary(month, year);
}
async function deleteCategory(id) {
    if (!confirm('Delete this category?')) return;
    await fetch(`${API}/categories/${id}`, {
        method: 'DELETE',
        headers: authHeaders()
    });
    await loadCategories();
    const month = document.getElementById('month-input').value;
    const year = document.getElementById('year-input').value;
    await loadSummary(month, year);
}   

async function deleteBudget(id) {
    if (!confirm('Delete this budget?')) return;
    await fetch(`${API}/budgets/${id}`, {
        method: 'DELETE',
        headers: authHeaders()
    });
    const month = document.getElementById('month-input').value;
    const year = document.getElementById('year-input').value;
    await loadSummary(month, year);
}