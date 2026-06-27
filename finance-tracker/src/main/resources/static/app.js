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

        // Budget progress bars
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
                        <span>$${b.actualSpending.toFixed(2)} / $${b.budgetLimit.toFixed(2)}</span>
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
                    <span class="transaction-amount ${isIncome ? 'income' : 'expense'}">
                        ${isIncome ? '+' : '-'}$${t.amount.toFixed(2)}
                    </span>
                </div>
            `;
        });
    } catch (e) {
        console.error('Error loading transactions', e);
    }
}