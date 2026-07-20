// js/app.js

const app = {
    // 1. Verify User Session and Role
    async checkAuth(allowedRoles = []) {
        const token = localStorage.getItem('token');
        if (!token) {
            window.location.href = '/login.html';
            return null;
        }

        try {
            const user = await api.request('/users/me', 'GET');

            // Normalize role for frontend checks
            user.role = user.role.startsWith('ROLE_') ? user.role : 'ROLE_' + user.role;

            // Check if user role is allowed on this page
            if (allowedRoles.length > 0 && !allowedRoles.includes(user.role)) {
                alert("You don't have permission to access this page.");
                this.redirectBasedOnRole(user.role);
                return null;
            }

            // Display user name in navbar if element exists
            const userNameEl = document.getElementById('nav-user-name');
            if (userNameEl) userNameEl.innerText = user.fullName;

            return user;
        } catch (error) {
            console.error("Auth check failed:", error);
            localStorage.removeItem('token');
            window.location.href = '/login.html';
            return null;
        }
    },

    // 2. Redirect fallback if someone visits the wrong dashboard
    redirectBasedOnRole(role) {
        if (role === 'ROLE_USER') window.location.href = '/dashboard.html';
        else if (role === 'ROLE_COMPANY') window.location.href = '/recruiter-dashboard.html';
        else if (role === 'ROLE_ADMIN') window.location.href = '/admin-dashboard.html';
        else window.location.href = '/login.html';
    },

    // 3. Global Logout Function
    logout() {
        localStorage.removeItem('token');
        window.location.href = '/login.html';
    }
};
// ==========================================
// NOTIFICATION ENGINE
// ==========================================

document.addEventListener('DOMContentLoaded', () => {
    // Bulletproof Dropdown Toggle
    const toggle = document.getElementById('notificationDropdownToggle');
    const menu = document.getElementById('notificationMenu');

    if (toggle && menu) {
        toggle.onclick = function(event) {
            event.stopPropagation(); // Prevents click from bubbling up
            menu.classList.toggle('hidden');
        };

        document.addEventListener('click', function(event) {
            if (!toggle.contains(event.target) && !menu.contains(event.target)) {
                menu.classList.add('hidden');
            }
        });
    }

    if (localStorage.getItem('token')) {
        loadNotifications();
    }
});

async function loadNotifications() {
    try {
        const notifications = await api.request('/notifications', 'GET');
        const badge = document.getElementById('unreadBadge');
        const list = document.getElementById('notificationList');

        list.innerHTML = '';
        let unreadCount = 0;

        if (!notifications || notifications.length === 0) {
            list.innerHTML = '<div class="p-4 text-center text-sm text-slate-500">No new notifications</div>';
            badge.classList.add('hidden');
            return;
        }

        notifications.forEach(n => {
            if (n.status === 'UNREAD') unreadCount++;

            // Highlight unread notifications with a light blue background
            const bgClass = n.status === 'UNREAD' ? 'bg-blue-50/50' : 'bg-white';

            list.innerHTML += `
                <div class="p-3 border-b border-slate-50 hover:bg-slate-50 transition ${bgClass}">
                    <div class="text-sm font-semibold text-slate-800">${n.title}</div>
                    <div class="text-xs text-slate-600 mt-1">${n.message}</div>
                    <div class="text-[10px] text-slate-400 mt-2">${new Date(n.createdAt).toLocaleDateString()}</div>
                </div>
            `;
        });

        if (unreadCount > 0) {
            badge.innerText = unreadCount;
            badge.classList.remove('hidden');
        } else {
            badge.classList.add('hidden');
        }

    } catch (error) {
        console.error("Failed to load notifications:", error);
    }
}

async function markAllAsRead() {
    try {
        await api.request('/notifications/read-all', 'PATCH');
        loadNotifications(); // Reload list to remove highlights and badge
    } catch (error) {
        console.error("Failed to mark notifications as read:", error);
    }
}