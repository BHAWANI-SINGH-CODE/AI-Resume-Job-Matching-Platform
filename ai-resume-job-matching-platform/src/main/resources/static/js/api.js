const API_BASE_URL = '/api/v1';

const api = {
    // 1. Prepare Headers with JWT Token
    getHeaders: () => {
        const token = localStorage.getItem('token');
        const headers = {
            'Content-Type': 'application/json'
        };
        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }
        return headers;
    },

    // 2. Standard API Request (GET, POST, PUT, DELETE, PATCH)
    async request(endpoint, method = 'GET', data = null) {
        const options = {
            method: method,
            headers: api.getHeaders()
        };

        if (data && method !== 'GET') {
            options.body = JSON.stringify(data);
        }

        try {
            const response = await fetch(`${API_BASE_URL}${endpoint}`, options);

            // Global Security: Redirect to login if token is invalid or missing
            if (response.status === 401 || response.status === 403) {
                localStorage.removeItem('token');
                window.location.href = '/login.html';
                throw new Error('Session expired. Please login again.');
            }

            const json = await response.json();

            // Check your backend ApiResponse format
            if (!response.ok) {
                throw new Error(json.message || 'API Request Failed');
            }

            // Your backend sends { success: true, message: "...", data: {...} }
            return json.data !== undefined ? json.data : json;
        } catch (error) {
            console.error(`[API Error] ${method} ${endpoint}:`, error);
            throw error;
        }
    },

    // 3. Special Request for Resume File Uploads
    async uploadFile(endpoint, file) {
        const token = localStorage.getItem('token');
        const formData = new FormData();
        formData.append('file', file);

        try {
            const response = await fetch(`${API_BASE_URL}${endpoint}`, {
                method: 'POST',
                headers: {
                    // NOTE: Do NOT set Content-Type here.
                    // Browser automatically sets it to 'multipart/form-data' with the correct boundary.
                    'Authorization': `Bearer ${token}`
                },
                body: formData
            });

            if (response.status === 401 || response.status === 403) {
                localStorage.removeItem('token');
                window.location.href = '/login.html';
                throw new Error('Unauthorized');
            }

            const json = await response.json();
            if (!response.ok) {
                throw new Error(json.message || 'File upload failed');
            }
            return json.data;
        } catch (error) {
            console.error(`[Upload Error] POST ${endpoint}:`, error);
            throw error;
        }
    }
};