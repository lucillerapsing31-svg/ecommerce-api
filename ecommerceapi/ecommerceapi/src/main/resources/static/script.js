// CONFIGURATION
const API_BASE_URL = 'http://localhost:8080/api/v1/products';
const SIGNUP_API_URL = 'http://localhost:8080/api/users';

// RUN WHEN PAGE LOADS
document.addEventListener('DOMContentLoaded', () => {
    checkUrlAndLoad();
    setupEventListeners();
    updateCartCount();
    displayCartItems();
    setupSignupForm();
    displayUserName();
});

//CHECK URL FOR CATEGORY & LOAD
function checkUrlAndLoad() {
    const urlParams = new URLSearchParams(window.location.search);
    const categoryFromUrl = urlParams.get('category');
    
    if (categoryFromUrl) {
        loadProductsByCategory(categoryFromUrl);
    } else {
        loadAllProducts();
    }
}

async function fetchProducts() {
    try {
        const response = await fetch(API_BASE_URL);

        // --- ERROR HANDLING: Check response status ---
        if (!response.ok) {
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
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const products = await response.json();
        const productGrid = document.querySelector('.product-grid:not(#featured-products):not(#discounted-products)');
        
        if(productGrid) {
            //EMPTY STATE HANDLING
            if (products.length === 0) {
                productGrid.innerHTML = '<p style="color:gray; text-align:center; width:100%; padding: 20px;">No products available in this category.</p>';
            } else {
                displayProducts(products);
            }
        }

    } catch (error) {
        console.error('Error loading category:', error.message);
        const productGrid = document.querySelector('.product-grid:not(#featured-products):not(#discounted-products)');
        if(productGrid) {
            productGrid.innerHTML = '<p style="color:red; text-align:center; width:100%;">Failed to load category.</p>';
        }
    }
}

//DISPLAY ALL PRODUCTS
function displayProducts(products) {
    const productGrid = document.querySelector('.product-grid:not(#featured-products):not(#discounted-products)');
    if (!productGrid) return;

    // EMPTY STATE HANDLING
    if (!products || products.length === 0) {
        productGrid.innerHTML = '<p style="color:gray; text-align:center; width:100%; padding: 20px;">No products available.</p>';
        return;
    }

    //DYNAMIC RENDERING
    productGrid.innerHTML = products.map(product => `
        <div class="product-card">
            <div class="product-image">
                <img src="${product.imageUrl || 'https://via.placeholder.com/200x200?text=No+Image'}" alt="${product.name}">
            </div>
            <h3>${product.name}</h3>
            <p class="category">${product.category ? product.category.name : 'Uncategorized'}</p>
            <p class="description">${product.description}</p>
            <p class="price">₱${product.price.toFixed(2)}</p>
            <button class="add-to-cart" data-id="${product.id}">Add to Cart</button>
        </div>
    `).join('');

    attachCartListeners();
}

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
function setupEventListeners() {
    const applyBtn = document.getElementById('apply-filters');
    const resetBtn = document.getElementById('reset-filters');
    const form = document.getElementById('filter-form');
    const checkoutBtn = document.getElementById('checkout-btn');

    if(applyBtn) applyBtn.addEventListener('click', applyFilters);
    
    if(resetBtn) resetBtn.addEventListener('click', () => {
        if(form) form.reset();
        loadAllProducts();
    });

    if(checkoutBtn) {
        checkoutBtn.addEventListener('click', () => {
            clearCart();
            alert('Thank you for your purchase!');
        });
    }
}

//APPLY FILTERS
async function applyFilters() {
    try {
        const selectedCategories = Array.from(document.querySelectorAll('input[name="category"]:checked'))
                                      .map(cb => cb.value);
        const selectedPrice = document.querySelector('input[name="price"]:checked')?.value;

        let url = API_BASE_URL;

        if (selectedCategories.length > 0) {
            // Also fix capitalization here for filter checkboxes
            const formattedCategory = selectedCategories[0].charAt(0).toUpperCase() + selectedCategories[0].slice(1);
            url = `${API_BASE_URL}/filter?filterType=category&filterValue=${formattedCategory}`;
        } 
        else if (selectedPrice && selectedPrice !== 'all') {
            let min, max;
            switch(selectedPrice) {
                case 'under30': min = 0; max = 30; break;
                case '30-60': min = 30; max = 60; break;
                case 'over60': min = 60; max = 999999; break;
            }
            url = `${API_BASE_URL}/filter?filterType=price&filterValue=${min},${max}`;
        }

        const response = await fetch(url);
        if (!response.ok) throw new Error('Filter error');
        const products = await response.json();

        const productGrid = document.querySelector('.product-grid:not(#featured-products):not(#discounted-products)');
        if(productGrid) {
            if (products.length === 0) {
                productGrid.innerHTML = '<p style="color:gray; text-align:center; width:100%;">No products found.</p>';
            } else {
                displayProducts(products);
            }
        }

    } catch (error) {
        console.error('Error applying filters:', error);
    }
}

//CART FUNCTIONALITY
function attachCartListeners() {
    document.querySelectorAll('.add-to-cart').forEach(button => {
        button.addEventListener('click', (e) => {
            addToCart(e.target.dataset.id);
        });
    });
}

function addToCart(productId) {
    let cart = JSON.parse(localStorage.getItem('cart')) || [];
    const existing = cart.find(item => item.id == productId);
    
    if (existing) existing.quantity++;
    else cart.push({ id: parseInt(productId), quantity: 1 });

    localStorage.setItem('cart', JSON.stringify(cart));
    updateCartCount();
    alert('Added to cart!');
}

function updateCartCount() {
    const cart = JSON.parse(localStorage.getItem('cart')) || [];
    const countEl = document.getElementById('cart-count');
    if(countEl) {
        const total = cart.reduce((sum, i) => sum + i.quantity, 0);
        countEl.textContent = total > 0 ? `(${total})` : '(0)';
    }
}

function clearCart() {
    localStorage.removeItem('cart');
    updateCartCount();
    const cartList = document.getElementById('cart-list');
    if(cartList) cartList.innerHTML = '<p>Your cart is empty.</p>';
    const totalEl = document.getElementById('total');
    if(totalEl) totalEl.textContent = '0.00';
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
    .then(allProducts => {
        let html = '';
        let grandTotal = 0;

        cart.forEach(item => {
            const product = allProducts.find(p => p.id == item.id);
            if(product) {
                const subtotal = product.price * item.quantity;
                grandTotal += subtotal;

                html += `
                <li class="cart-item">
                    <span>${product.name}</span>
                    <span>x${item.quantity}</span>
                    <span>₱${subtotal.toFixed(2)}</span>
                </li>
                `;
            }
        });

        cartList.innerHTML = html;
        if(totalEl) totalEl.textContent = `${grandTotal.toFixed(2)}`;
    });
}

//SIGNUP FUNCTIONALITY
function setupSignupForm() {
    const signupForm = document.querySelector('.signup-form');
    
    if(!signupForm) return;

    signupForm.addEventListener('submit', async function(e) {
        e.preventDefault();

        const fullName = document.getElementById('fullname').value;
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirm-password').value;

        if(password !== confirmPassword) {
            alert('Passwords do not match!');
            return;
        }

        const userData = {
            fullName: fullName,
            email: email,
            password: password
        };

        try {
            const response = await fetch(SIGNUP_API_URL, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(userData)
            });

            if(response.ok) {
                localStorage.setItem('userName', fullName);
                alert('Account created successfully!');
                signupForm.reset();
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

//DISPLAY USER NAME
function displayUserName() {
    const welcomeText = document.querySelector('h1');
    const userName = localStorage.getItem('userName');
    
    if(welcomeText && userName) {
        welcomeText.innerHTML = `Welcome back, <span style="color: #e67e22;">${userName}</span>!`;
    }
}
