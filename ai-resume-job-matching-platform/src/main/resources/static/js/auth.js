// --- Role Selection Logic for Register Page ---
function selectRole(role) {
    document.getElementById('role').value = role;

    const btnCandidate = document.getElementById('tab-candidate');
    const btnRecruiter = document.getElementById('tab-recruiter');

    if (role === 'USER') {
        btnCandidate.className = "flex-1 py-2 text-sm font-semibold rounded-md bg-white shadow text-blue-600 transition-all";
        btnRecruiter.className = "flex-1 py-2 text-sm font-semibold rounded-md text-slate-500 hover:text-slate-700 transition-all";
    } else {
        btnRecruiter.className = "flex-1 py-2 text-sm font-semibold rounded-md bg-white shadow text-blue-600 transition-all";
        btnCandidate.className = "flex-1 py-2 text-sm font-semibold rounded-md text-slate-500 hover:text-slate-700 transition-all";
    }
}

// --- Registration Logic ---
const registerForm = document.getElementById('registerForm');
if (registerForm) {
    registerForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const fullName = document.getElementById('fullName').value;
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirmPassword').value;
        const role = document.getElementById('role').value;
        const errorMsg = document.getElementById('error-msg');
        const btn = document.getElementById('registerBtn');

        if (password !== confirmPassword) {
            errorMsg.innerText = "Passwords do not match!";
            errorMsg.classList.remove('hidden');
            return;
        }

        try {
            btn.innerText = "Creating Account...";
            btn.disabled = true;
            errorMsg.classList.add('hidden');

            const payload = { fullName, email, password, confirmPassword, role };

            // Calling POST /api/v1/auth/register
            await api.request('/auth/register', 'POST', payload);

            // Redirect to login on success
            alert("Registration successful! Please login.");
            window.location.href = '/login.html';

        } catch (error) {
            errorMsg.innerText = error.message;
            errorMsg.classList.remove('hidden');
            btn.innerText = "Create Account";
            btn.disabled = false;
        }
    });
}

// --- Login Logic ---
// --- Login Logic ---
const loginForm = document.getElementById('loginForm');
if (loginForm) {
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        const errorMsg = document.getElementById('error-msg');
        const btn = document.getElementById('loginBtn');

        try {
            btn.innerText = "Logging in...";
            btn.disabled = true;
            errorMsg.classList.add('hidden');

            // 1. Call Login API
            const loginData = await api.request('/auth/login', 'POST', { email, password });

            // 2. Save Token (Handles different backend response structures safely)
            const token = loginData.accessToken || loginData.token || loginData;
            localStorage.setItem('token', token);

            // 3. Fetch User Profile to get Role
            const userProfile = await api.request('/users/me', 'GET');

            if (!userProfile || !userProfile.role) {
                throw new Error("Could not retrieve user profile details.");
            }

            // 4. Normalize and Redirect based on Role
            const userRole = userProfile.role.startsWith('ROLE_') ? userProfile.role : 'ROLE_' + userProfile.role;
            console.log("Logged in successfully as:", userRole);

            if (userRole === 'ROLE_USER') {
                window.location.href = '/dashboard.html';
            } else if (userRole === 'ROLE_COMPANY') {
                window.location.href = '/recruiter-dashboard.html';
            } else if (userRole === 'ROLE_ADMIN') {
                window.location.href = '/admin-dashboard.html';
            } else {
                throw new Error("Invalid User Role received.");
            }

        } catch (error) {
            console.error("Login Error:", error); // Logs exact error in console for future
            errorMsg.innerText = error.message || "Invalid credentials. Please try again.";
            errorMsg.classList.remove('hidden');
        } finally {
            // Always reset button state
            btn.innerText = "Login";
            btn.disabled = false;
        }
    });
}