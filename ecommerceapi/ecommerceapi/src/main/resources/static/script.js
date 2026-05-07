<<<<<<< HEAD
// ======================================
// CONFIGURATION
// ======================================
=======
// CONFIGURATION
>>>>>>> 32c9a677cdae3622cc79aab8509f0fda203e5eb5
const API_BASE_URL = 'http://localhost:8080/api/v1/products';
const SIGNUP_API_URL = 'http://localhost:8080/api/v1/auth/register';

<<<<<<< HEAD
// ======================================
// HELPER FUNCTIONS FOR CSRF & HEADERS
// ======================================
function getCookie(name) {
    let value = "; " + document.cookie;
    let parts = value.split("; " + name + "=");
    if (parts.length === 2) return parts.pop().split(";").shift();
}

function getAuthHeaders() {
    const token = getCookie('XSRF-TOKEN');
    const headers = {
        'Content-Type': 'application/json'
    };
    if (token) {
        headers['X-XSRF-TOKEN'] = token;
    }
    return headers;
}

// ======================================
// RUN WHEN PAGE LOADS
// ======================================
=======
// RUN WHEN PAGE LOADS
>>>>>>> 32c9a677cdae3622cc79aab8509f0fda203e5eb5
document.addEventListener('DOMContentLoaded', () => {
    console.log('Page loaded, initializing...');
    console.log('API_BASE_URL:', API_BASE_URL);
    
    checkUrlAndLoad();
    setupEventListeners();
    updateCartCount();
    displayCartItems();
    setupSignupForm();
    setupLoginForm();
    displayUserName();
    setupAdminFeatures();
    
    // LOAD PRODUCTS FOR HOMEPAGE
    loadFeaturedProducts();
    loadDiscountedProducts();
    
    // Load CSRF Cookie
    fetch('http://localhost:8080/login', { credentials: 'include' });
});

<<<<<<< HEAD
// ======================================
// CHECK URL FOR CATEGORY & LOAD
// ======================================
=======
//CHECK URL FOR CATEGORY & LOAD
>>>>>>> 32c9a677cdae3622cc79aab8509f0fda203e5eb5
function checkUrlAndLoad() {
    const urlParams = new URLSearchParams(window.location.search);
    const categoryFromUrl = urlParams.get('category');
    
    if (categoryFromUrl) {
        loadProductsByCategory(categoryFromUrl);
    } else {
        loadAllProducts();
    }
}

// ======================================
// FETCH PRODUCTS FROM BACKEND
// ======================================
async function fetchProducts() {
    try {
        const response = await fetch(API_BASE_URL, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            },
            cache: "no-store"
        });

        if (!response.ok) {
<<<<<<< HEAD
=======
            if (response.status === 404) {
                throw new Error('Products not found (Error 404)');
            }
            if (response.status === 500) {
                throw new Error('Server error (Error 500)');
            }
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data = await response.json();
        return data;

    } catch (error) {
        // --- Log specific error messages ---
        console.error('Error fetching products:', error.message);
        throw error; // Re-throw to let the calling function handle it
    }
}

//LOAD ALL PRODUCTS
async function loadAllProducts() {
    try {
        const products = await fetchProducts();
        displayProducts(products);
        displayFeaturedProducts(products);

    } catch (error) {
        console.error('Failed to load products:', error);
        const productGrid = document.querySelector('.product-grid:not(#featured-products):not(#discounted-products)');
        if(productGrid) {
            productGrid.innerHTML = '<p style="color:red; text-align:center; width:100%;">Failed to load products. Please try again later.</p>';
        }
    }
}

//LOAD BY CATEGORY (For Books, Clothing, etc.)
async function loadProductsByCategory(category) {
    try {
        //FIX: Capitalize first letter to match database exactly
        const formattedCategory = category.charAt(0).toUpperCase() + category.slice(1);
        
        const url = `${API_BASE_URL}/filter?filterType=category&filterValue=${formattedCategory}`;
        const response = await fetch(url);
        
        if (!response.ok) {
            if (response.status === 404) {
                throw new Error('Category not found (Error 404)');
            }
>>>>>>> 32c9a677cdae3622cc79aab8509f0fda203e5eb5
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const products = await response.json();
<<<<<<< HEAD
        console.log("✅ Products fetched:", products.length);
        return products;
=======
        const productGrid = document.querySelector('.product-grid:not(#featured-products):not(#discounted-products)');
        
        if(productGrid) {
            //EMPTY STATE HANDLING
            if (products.length === 0) {
                productGrid.innerHTML = '<p style="color:gray; text-align:center; width:100%; padding: 20px;">No products available in this category.</p>';
            } else {
                displayProducts(products);
            }
        }

>>>>>>> 32c9a677cdae3622cc79aab8509f0fda203e5eb5
    } catch (error) {
        console.error('❌ Error fetching products:', error.message);
        return [];
    }
}

// ======================================
// FUNCTIONS FOR HOMEPAGE
// ======================================

// LOAD FEATURED PRODUCTS
async function loadFeaturedProducts() {
    const container = document.getElementById('featured-products');
    if (!container) {
        console.log('❌ Featured products container not found!');
        return;
    }

    try {
        const products = await fetchProducts();
        
        if (!products || products.length === 0) {
            container.innerHTML = '<p style="color:gray; text-align:center;">No products available.</p>';
            return;
        }
        
        // Display first 4 products as featured
        const featured = products.slice(0, 4);
        console.log('📦 Showing featured products:', featured.length);
        
        container.innerHTML = featured.map(product => `
            <div class="product-card">
                <div class="product-image">
                    <img src="${product.imageUrl || 'https://via.placeholder.com/200x200?text=Product'}" alt="${product.name}">
                </div>
                <h3>${product.name}</h3>
                <p class="price">₱${product.price.toFixed(2)}</p>
                <button class="add-to-cart" data-id="${product.id}">Add to Cart</button>
            </div>
        `).join('');
        
        attachCartListeners();
    } catch (error) {
        console.error('❌ Error loading featured products:', error);
        container.innerHTML = '<p style="color:red; text-align:center;">Failed to load products. Please make sure the backend is running on port 8080.</p>';
    }
}

// LOAD DISCOUNTED PRODUCTS
async function loadDiscountedProducts() {
    const container = document.getElementById('discounted-products');
    if (!container) {
        console.log('❌ Discounted products container not found!');
        return;
    }

    try {
        const products = await fetchProducts();
        
        if (!products || products.length === 0) {
            container.innerHTML = '<p style="color:gray; text-align:center;">No products available.</p>';
            return;
        }
        
        // Show products under 100 as discounted
        const discounted = products.filter(p => p.price < 100);
        console.log('🏷️ Discounted products:', discounted.length);
        
        if (discounted.length === 0) {
            container.innerHTML = '<p style="color:gray; text-align:center;">No discounted products available.</p>';
            return;
        }
        
        container.innerHTML = discounted.map(product => `
            <div class="product-card">
                <div class="product-image">
                    <img src="${product.imageUrl || 'https://via.placeholder.com/200x200?text=Product'}" alt="${product.name}">
                </div>
                <h3>${product.name}</h3>
                <p class="price">₱${product.price.toFixed(2)}</p>
                <button class="add-to-cart" data-id="${product.id}">Add to Cart</button>
            </div>
        `).join('');
        
        attachCartListeners();
    } catch (error) {
        console.error('❌ Error loading discounted products:', error);
        container.innerHTML = '<p style="color:red; text-align:center;">Failed to load discounted products.</p>';
    }
}

// ======================================
// STANDARD PRODUCT FUNCTIONS
// ======================================

// LOAD ALL PRODUCTS
async function loadAllProducts() {
    try {
        const products = await fetchProducts();
        displayProducts(products);
    } catch (error) {
        console.error('Failed to load products:', error);
        const productGrid = document.querySelector('.product-grid');
        if(productGrid) {
            productGrid.innerHTML = '<p style="color:red; text-align:center; width:100%;">Failed to load products.</p>';
        }
    }
}

<<<<<<< HEAD
// LOAD BY CATEGORY
async function loadProductsByCategory(category) {
    try {
        const products = await fetchProducts();
        const filtered = products.filter(p => 
            p.category && p.category.name && p.category.name.toLowerCase() === category.toLowerCase()
        );
        
        const productGrid = document.querySelector('.product-grid');
        if(productGrid) {
            if(filtered.length === 0) {
                productGrid.innerHTML = '<p style="color:gray; text-align:center;">No products in this category.</p>';
            } else {
                displayProducts(filtered);
            }
        }
    } catch (error) {
        console.error('Error loading category:', error);
    }
}

// DISPLAY ALL PRODUCTS
=======
//DISPLAY ALL PRODUCTS
>>>>>>> 32c9a677cdae3622cc79aab8509f0fda203e5eb5
function displayProducts(products) {
    const productGrid = document.querySelector('.product-grid');
    if (!productGrid) return;

<<<<<<< HEAD
=======
    // EMPTY STATE HANDLING
>>>>>>> 32c9a677cdae3622cc79aab8509f0fda203e5eb5
    if (!products || products.length === 0) {
        productGrid.innerHTML = '<p style="color:gray; text-align:center; width:100%; padding: 20px;">No products available.</p>';
        return;
    }

<<<<<<< HEAD
    const userEmail = localStorage.getItem('userName');
    const isAdmin = userEmail && userEmail.includes('admin');

=======
    //DYNAMIC RENDERING
>>>>>>> 32c9a677cdae3622cc79aab8509f0fda203e5eb5
    productGrid.innerHTML = products.map(product => `
        <div class="product-card">
            <div class="product-image">
                <img src="${product.imageUrl || 'https://via.placeholder.com/200x200?text=Product'}" alt="${product.name}">
            </div>
            <h3>${product.name}</h3>
            <p class="description">${product.description || 'No description available'}</p>
            <p class="price">₱${product.price.toFixed(2)}</p>
            <button class="add-to-cart" data-id="${product.id}">Add to Cart</button>
            ${isAdmin ? `<button class="delete-btn" data-id="${product.id}" style="background:red; color:white; margin-top:5px;">Delete</button>` : ''}
        </div>
    `).join('');

    attachCartListeners();
    attachDeleteListeners();
}

<<<<<<< HEAD
// ======================================
// EVENT LISTENERS
// ======================================
=======
//DISPLAY FEATURED & DISCOUNTED
function displayFeaturedProducts(products) {
    const featuredContainer = document.getElementById('featured-products');
    const discountedContainer = document.getElementById('discounted-products');
    
    if (featuredContainer) {
        const featured = products.slice(0, 4);
        featuredContainer.innerHTML = featured.map(product => `
            <div class="product-card">
                <div class="product-image">
                    <img src="${product.imageUrl || 'https://via.placeholder.com/200x200?text=No+Image'}" alt="${product.name}">
                </div>
                <h3>${product.name}</h3>
                <p class="price">₱${product.price.toFixed(2)}</p>
                <button class="add-to-cart" data-id="${product.id}">Add to Cart</button>
            </div>
        `).join('');
    }

    if (discountedContainer) {
        const discounted = products.slice(4, 8);
        discountedContainer.innerHTML = discounted.map(product => `
            <div class="product-card">
                <div class="product-image">
                    <img src="${product.imageUrl || 'https://via.placeholder.com/200x200?text=No+Image'}" alt="${product.name}">
                </div>
                <h3>${product.name}</h3>
                <p class="price">₱${product.price.toFixed(2)}</p>
                <button class="add-to-cart" data-id="${product.id}">Add to Cart</button>
            </div>
        `).join('');
    }
    
    attachCartListeners();
}

//EVENT LISTENERS
>>>>>>> 32c9a677cdae3622cc79aab8509f0fda203e5eb5
function setupEventListeners() {
    const applyBtn = document.getElementById('apply-filters');
    const resetBtn = document.getElementById('reset-filters');
    const checkoutBtn = document.getElementById('checkout-btn');

    if(applyBtn) applyBtn.addEventListener('click', applyFilters);
    if(resetBtn) resetBtn.addEventListener('click', () => { loadAllProducts(); });
    if(checkoutBtn) checkoutBtn.addEventListener('click', () => { clearCart(); alert('Thank you!'); });
}

<<<<<<< HEAD
// APPLY FILTERS
=======
//APPLY FILTERS
>>>>>>> 32c9a677cdae3622cc79aab8509f0fda203e5eb5
async function applyFilters() {
    try {
        const selectedCategories = Array.from(document.querySelectorAll('input[name="category"]:checked')).map(cb => cb.value);
        const selectedPrice = document.querySelector('input[name="price"]:checked')?.value;

        const products = await fetchProducts();
        let filtered = products;

        if(selectedCategories.length > 0) {
            const cat = selectedCategories[0].toLowerCase();
            filtered = filtered.filter(p => p.category && p.category.name && p.category.name.toLowerCase() === cat);
        } 
        
        if(selectedPrice && selectedPrice !== 'all') {
            let min, max;
            if(selectedPrice === 'under30') { min=0; max=30; }
            if(selectedPrice === '30-60') { min=30; max=60; }
            if(selectedPrice === 'over60') { min=60; max=999999; }
            filtered = filtered.filter(p => p.price >= min && p.price <= max);
        }

        const grid = document.querySelector('.product-grid');
        if(grid) {
            if(filtered.length === 0) {
                grid.innerHTML = '<p style="color:gray;">No products found.</p>';
            } else {
                displayProducts(filtered);
            }
        }
    } catch(e) { console.error(e); }
}

<<<<<<< HEAD
// ======================================
// CART FUNCTIONALITY
// ======================================
=======
//CART FUNCTIONALITY
>>>>>>> 32c9a677cdae3622cc79aab8509f0fda203e5eb5
function attachCartListeners() {
    document.querySelectorAll('.add-to-cart').forEach(btn => {
        btn.removeEventListener('click', handleAddToCart);
        btn.addEventListener('click', handleAddToCart);
    });
}

function handleAddToCart(e) {
    const productId = e.target.dataset.id;
    addToCart(productId);
}

function addToCart(productId) {
    let cart = JSON.parse(localStorage.getItem('cart')) || [];
    const existing = cart.find(item => item.id == productId);
    existing ? existing.quantity++ : cart.push({id: parseInt(productId), quantity: 1});
    localStorage.setItem('cart', JSON.stringify(cart));
    updateCartCount();
    alert('Added to cart!');
}

function updateCartCount() {
    const cart = JSON.parse(localStorage.getItem('cart')) || [];
    const count = cart.reduce((sum, i) => sum + i.quantity, 0);
    const el = document.getElementById('cart-count');
    if(el) el.textContent = `(${count})`;
}

function clearCart() { 
    localStorage.removeItem('cart'); 
    updateCartCount(); 
}

function displayCartItems() {
    const cartList = document.getElementById('cart-list');
    const totalEl = document.getElementById('total');
    if(!cartList) return;

    let cart = JSON.parse(localStorage.getItem('cart')) || [];
    if(cart.length === 0) {
        cartList.innerHTML = '<p>Your cart is empty.</p>';
        if(totalEl) totalEl.textContent = '0.00';
        return;
    }

    fetch(API_BASE_URL)
    .then(res => res.json())
    .then(products => {
        let html = '';
        let total = 0;
        cart.forEach(item => {
            const p = products.find(prod => prod.id == item.id);
            if(p) {
                const subtotal = p.price * item.quantity;
                total += subtotal;
                html += `<li class="cart-item"><span>${p.name}</span><span>x${item.quantity}</span><span>₱${subtotal.toFixed(2)}</span></li>`;
            }
        });
        cartList.innerHTML = html;
        if(totalEl) totalEl.textContent = total.toFixed(2);
    })
    .catch(err => {
        console.error(err);
        cartList.innerHTML = '<p>Error loading cart.</p>';
    });
}

<<<<<<< HEAD
// ======================================
// SIGNUP FUNCTIONALITY
// ======================================
=======
//SIGNUP FUNCTIONALITY
>>>>>>> 32c9a677cdae3622cc79aab8509f0fda203e5eb5
function setupSignupForm() {
    const form = document.querySelector('.signup-form');
    if(!form) return;

    form.addEventListener('submit', async function(e) {
        e.preventDefault();

        const data = {
            fullname: document.getElementById('fullname').value,
            email: document.getElementById('email').value,
            password: document.getElementById('password').value,
            confirmPassword: document.getElementById('confirm-password').value,
            role: document.getElementById('role').value
        };

        if(data.password !== data.confirmPassword) {
            alert("Passwords do not match!");
            return;
        }

        try {
            const response = await fetch(SIGNUP_API_URL, {
                method: 'POST',
                headers: getAuthHeaders(),
                body: JSON.stringify(data),
                credentials: 'include'
            });

            if(response.ok) {
                localStorage.setItem('userName', data.fullname);
                alert('Account created successfully!');
                window.location.href = "account.html";
            } else {
                alert('Error creating account.');
            }
        } catch (error) {
            console.error('Error:', error);
            alert('Cannot connect to server.');
        }
    });
}

<<<<<<< HEAD
// ======================================
// LOGIN FUNCTIONALITY
// ======================================
function setupLoginForm() {
    const form = document.querySelector('.login-form');
    if(!form) return;

    form.addEventListener('submit', async function(e) {
        e.preventDefault();

        const email = document.getElementById('login-email').value;
        const password = document.getElementById('login-password').value;

        const loginData = new URLSearchParams();
        loginData.append('email', email);
        loginData.append('password', password);

        try {
            const response = await fetch('http://localhost:8080/login', {
                method: 'POST',
                headers: { 
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'X-XSRF-TOKEN': getCookie('XSRF-TOKEN')
                },
                body: loginData,
                credentials: 'include'
            });

            if(response.ok || response.redirected) {
                localStorage.setItem('userName', email);
                alert('Login successful!');
                window.location.href = "products.html";
            } else {
                alert('Login failed! Check email and password.');
            }
        } catch (error) {
            console.error('Error:', error);
            alert('Cannot connect to server.');
        }
    });
}

// ======================================
// LOGOUT FUNCTIONALITY
// ======================================
function logoutUser() {
    fetch('http://localhost:8080/logout', {
        method: 'POST',
        credentials: 'include',
        headers: { 'X-XSRF-TOKEN': getCookie('XSRF-TOKEN') }
    }).then(() => {
        localStorage.removeItem('userName');
        window.location.href = "login.html";
    });
}

// ======================================
// DISPLAY USER NAME
// ======================================
=======
//DISPLAY USER NAME
>>>>>>> 32c9a677cdae3622cc79aab8509f0fda203e5eb5
function displayUserName() {
    const name = localStorage.getItem('userName');
    const h1 = document.querySelector('h1');
    if(h1 && name) {
        h1.innerHTML = `Welcome back, <span style="color: #e67e22;">${name}</span>!`;
    }
}

// ======================================
// ADMIN FEATURES - ADD & DELETE
// ======================================
function setupAdminFeatures() {
    const userEmail = localStorage.getItem('userName');
    if(!userEmail || !userEmail.includes('admin')) return;

    const adminSection = document.getElementById('admin-section');
    if(adminSection) adminSection.style.display = 'block';

    const form = document.getElementById('add-product-form');
    if(form) {
        form.addEventListener('submit', async function(e) {
            e.preventDefault();

            const name = document.getElementById('product-name').value;
            const price = parseFloat(document.getElementById('product-price').value);
            const desc = document.getElementById('product-desc').value;
            const catName = document.getElementById('product-category').value;

            let catId = 1;
            if(catName === "Electronics") catId = 2;
            if(catName === "Clothing") catId = 3;
            if(catName === "Books") catId = 4;
            if(catName === "Home & Kitchen") catId = 5;

            const data = {
                name: name,
                price: price,
                description: desc,
                stockQuantity: 10,
                imageUrl: "",
                category: { id: catId, name: catName }
            };

            try {
                const response = await fetch(API_BASE_URL, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(data),
                    cache: "no-store"
                });

                if(response.ok) {
                    alert('✅ Product added successfully!');
                    form.reset();
                    loadAllProducts();
                    loadFeaturedProducts();
                } else {
                    alert('❌ Error! Status: ' + response.status);
                }
            } catch (error) {
                console.error('Error:', error);
                alert('Cannot connect to server.');
            }
        });
    }
}

// DELETE FUNCTION
function attachDeleteListeners() {
    document.querySelectorAll('.delete-btn').forEach(button => {
        button.removeEventListener('click', handleDelete);
        button.addEventListener('click', handleDelete);
    });
}

async function handleDelete(e) {
    const productId = e.target.dataset.id;
    if(confirm('Are you sure you want to delete this product?')) {
        try {
            const response = await fetch(`${API_BASE_URL}/${productId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if(response.ok) {
                alert('🗑️ Product deleted!');
                loadAllProducts();
                loadFeaturedProducts();
                loadDiscountedProducts();
            } else {
                alert('❌ Error deleting product');
            }
        } catch (err) {
            console.error(err);
        }
    }
}
