/**
 * Основний JavaScript файл для Платформи моніторингу фермерських операцій
 */

document.addEventListener('DOMContentLoaded', function() {
    // Ініціалізація tooltips та popovers
    initTooltipsAndPopovers();
    
    // Ініціалізація графіків, якщо вони є на сторінці
    initCharts();
    
    // Налаштування авто-оновлення даних
    setupAutoRefresh();
    
    // Налаштування живого пошуку
    setupLiveSearch();
    
    // Налаштування перемикачів періодів для графіків
    setupTimeRangeSwitchers();

    // Налаштування сповіщень (toasts)
    setupToasts();

    // Плавне прокручування до якорів
    setupSmoothScrolling();
    
    // Анімація сторінки після завантаження
    setTimeout(function() {
        document.body.classList.add('loaded');
    }, 100);
});

/**
 * Ініціалізація підказок та випадаючих вікон
 */
function initTooltipsAndPopovers() {
    // Налаштування Bootstrap tooltips
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl, {
            delay: { show: 300, hide: 100 },
            animation: true
        });
    });
    
    // Налаштування Bootstrap popovers
    const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl, {
            trigger: 'focus',
            html: true
        });
    });
}

/**
 * Ініціалізація графіків з даними датчиків
 */
function initCharts() {
    // Перевірка наявності контейнера для графіка температури
    const temperatureChartCanvas = document.getElementById('temperatureChart');
    if (temperatureChartCanvas) {
        initTemperatureChart(temperatureChartCanvas);
    }
    
    // Перевірка наявності контейнера для графіка вологості
    const moistureChartCanvas = document.getElementById('moistureChart');
    if (moistureChartCanvas) {
        initMoistureChart(moistureChartCanvas);
    }
    
    // Перевірка наявності загального графіка
    const overviewChartCanvas = document.getElementById('overviewChart');
    if (overviewChartCanvas) {
        initOverviewChart(overviewChartCanvas);
    }

    // Анімація графіків при прокручуванні
    animateChartsOnScroll();
}

/**
 * Анімація графіків при прокручуванні до них
 */
function animateChartsOnScroll() {
    const charts = document.querySelectorAll('.chart-container');
    
    if (charts.length === 0) return;
    
    // Функція для перевірки, чи елемент видимий
    function isElementInViewport(el) {
        const rect = el.getBoundingClientRect();
        return (
            rect.top >= 0 &&
            rect.left >= 0 &&
            rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) &&
            rect.right <= (window.innerWidth || document.documentElement.clientWidth)
        );
    }
    
    // Функція, яка буде викликатися при прокручуванні
    function handleScroll() {
        charts.forEach(function(chart) {
            if (isElementInViewport(chart) && !chart.classList.contains('animated')) {
                chart.classList.add('animated');
                chart.style.opacity = '0';
                chart.style.transform = 'translateY(20px)';
                
                setTimeout(function() {
                    chart.style.transition = 'opacity 0.5s ease, transform 0.5s ease';
                    chart.style.opacity = '1';
                    chart.style.transform = 'translateY(0)';
                }, 100);
            }
        });
    }
    
    // Додавання обробника події прокручування
    window.addEventListener('scroll', handleScroll);
    
    // Викликаємо один раз для ініціалізації
    handleScroll();
}

/**
 * Ініціалізація графіка температури
 */
function initTemperatureChart(canvas) {
    // Отримання даних з data-атрибутів
    const labels = JSON.parse(canvas.getAttribute('data-labels') || '[]');
    const values = JSON.parse(canvas.getAttribute('data-values') || '[]');
    
    // Створення графіка
    new Chart(canvas, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: 'Температура (°C)',
                data: values,
                borderColor: '#ff6b6b',
                backgroundColor: 'rgba(255, 107, 107, 0.1)',
                borderWidth: 2,
                fill: true,
                tension: 0.4,
                pointRadius: 3,
                pointHoverRadius: 6,
                pointBackgroundColor: '#fff',
                pointHoverBackgroundColor: '#ff6b6b',
                pointBorderColor: '#ff6b6b',
                pointBorderWidth: 2
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            animation: {
                duration: 1500,
                easing: 'easeOutQuart'
            },
            scales: {
                y: {
                    beginAtZero: false,
                    grid: {
                        color: 'rgba(0, 0, 0, 0.05)'
                    },
                    title: {
                        display: true,
                        text: 'Температура (°C)',
                        font: {
                            weight: 'bold'
                        }
                    }
                },
                x: {
                    grid: {
                        display: false
                    },
                    title: {
                        display: true,
                        text: 'Час',
                        font: {
                            weight: 'bold'
                        }
                    }
                }
            },
            plugins: {
                legend: {
                    display: true,
                    position: 'top',
                    labels: {
                        usePointStyle: true,
                        pointStyle: 'circle',
                        padding: 15
                    }
                },
                tooltip: {
                    mode: 'index',
                    intersect: false,
                    backgroundColor: 'rgba(0, 0, 0, 0.7)',
                    titleFont: {
                        size: 14
                    },
                    bodyFont: {
                        size: 13
                    },
                    padding: 10,
                    cornerRadius: 4,
                    caretSize: 5
                }
            },
            interaction: {
                mode: 'nearest',
                axis: 'x',
                intersect: false
            }
        }
    });
}

/**
 * Ініціалізація графіка вологості
 */
function initMoistureChart(canvas) {
    // Отримання даних з data-атрибутів
    const labels = JSON.parse(canvas.getAttribute('data-labels') || '[]');
    const values = JSON.parse(canvas.getAttribute('data-values') || '[]');
    
    // Створення графіка
    new Chart(canvas, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: 'Вологість (%)',
                data: values,
                borderColor: '#4dabf7',
                backgroundColor: 'rgba(77, 171, 247, 0.1)',
                borderWidth: 2,
                fill: true,
                tension: 0.4,
                pointRadius: 3,
                pointHoverRadius: 6,
                pointBackgroundColor: '#fff',
                pointHoverBackgroundColor: '#4dabf7',
                pointBorderColor: '#4dabf7',
                pointBorderWidth: 2
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            animation: {
                duration: 1500,
                easing: 'easeOutQuart'
            },
            scales: {
                y: {
                    beginAtZero: false,
                    grid: {
                        color: 'rgba(0, 0, 0, 0.05)'
                    },
                    title: {
                        display: true,
                        text: 'Вологість (%)',
                        font: {
                            weight: 'bold'
                        }
                    }
                },
                x: {
                    grid: {
                        display: false
                    },
                    title: {
                        display: true,
                        text: 'Час',
                        font: {
                            weight: 'bold'
                        }
                    }
                }
            },
            plugins: {
                legend: {
                    display: true,
                    position: 'top',
                    labels: {
                        usePointStyle: true,
                        pointStyle: 'circle',
                        padding: 15
                    }
                },
                tooltip: {
                    mode: 'index',
                    intersect: false,
                    backgroundColor: 'rgba(0, 0, 0, 0.7)',
                    titleFont: {
                        size: 14
                    },
                    bodyFont: {
                        size: 13
                    },
                    padding: 10,
                    cornerRadius: 4,
                    caretSize: 5
                }
            },
            interaction: {
                mode: 'nearest',
                axis: 'x',
                intersect: false
            }
        }
    });
}

/**
 * Налаштування авто-оновлення даних на сторінці
 */
function setupAutoRefresh() {
    const refreshButton = document.getElementById('refreshDataButton');
    const autoRefreshToggle = document.getElementById('autoRefreshToggle');
    
    if (refreshButton) {
        refreshButton.addEventListener('click', function() {
            refreshData();
            
            // Анімація натискання кнопки
            this.classList.add('clicked');
            setTimeout(() => {
                this.classList.remove('clicked');
            }, 300);
        });
    }
    
    if (autoRefreshToggle) {
        let refreshInterval;
        
        autoRefreshToggle.addEventListener('change', function() {
            if (this.checked) {
                // Запуск авто-оновлення кожні 30 секунд
                refreshInterval = setInterval(refreshData, 30000);
                localStorage.setItem('autoRefresh', 'true');
                showToast('Авто-оновлення увімкнено', 'info');
            } else {
                // Зупинка авто-оновлення
                clearInterval(refreshInterval);
                localStorage.setItem('autoRefresh', 'false');
                showToast('Авто-оновлення вимкнено', 'info');
            }
        });
        
        // Перевірка збереженого налаштування
        const savedAutoRefresh = localStorage.getItem('autoRefresh');
        if (savedAutoRefresh === 'true') {
            autoRefreshToggle.checked = true;
            // Запуск авто-оновлення
            refreshInterval = setInterval(refreshData, 30000);
        }
    }
}

/**
 * Оновлення даних через AJAX
 * @param {string} url - URL для запиту даних (опціонально)
 * @param {Function} callback - функція зворотного виклику після отримання даних
 */
function refreshData(url, callback) {
    // Показуємо індикатор завантаження
    const refreshButton = document.getElementById('refreshDataButton');
    const refreshSpinner = document.getElementById('refreshSpinner');
    const loadingOverlay = document.querySelector('.loading-overlay');
    
    if (refreshButton && refreshSpinner) {
        refreshButton.disabled = true;
        refreshSpinner.classList.remove('d-none');
    }
    
    if (loadingOverlay) {
        loadingOverlay.classList.add('show');
    }
    
    // Визначаємо URL для запиту
    const requestUrl = url || window.location.pathname + '?refresh=true';
    
    // AJAX запит для оновлення даних
    fetch(requestUrl)
        .then(response => {
            if (!response.ok) {
                throw new Error('Помилка мережі або сервера');
            }
            return response.json();
        })
        .then(data => {
            // Оновлення даних на сторінці
            updatePageData(data);
            
            // Оновлення часу останньої синхронізації
            const dataTimestamp = document.getElementById('dataTimestamp');
            if (dataTimestamp) {
                const now = new Date();
                dataTimestamp.textContent = now.toLocaleDateString('uk-UA') + ' ' + now.toLocaleTimeString('uk-UA');
            }
            
            // Показ повідомлення про успішне оновлення
            showToast('Дані успішно оновлено', 'success');
            
            // Виклик функції зворотного виклику, якщо вона передана
            if (typeof callback === 'function') {
                callback(data);
            }
        })
        .catch(error => {
            console.error('Помилка оновлення даних:', error);
            showToast('Помилка оновлення даних: ' + error.message, 'danger');
        })
        .finally(() => {
            // Приховуємо індикатор завантаження
            if (refreshButton && refreshSpinner) {
                refreshButton.disabled = false;
                refreshSpinner.classList.add('d-none');
            }
            
            if (loadingOverlay) {
                loadingOverlay.classList.remove('show');
            }
            
            // Додаємо ефект оновлення для елементів
            const cards = document.querySelectorAll('.card');
            cards.forEach(card => {
                card.classList.add('highlight');
                setTimeout(() => {
                    card.classList.remove('highlight');
                }, 2000);
            });
        });
}

/**
 * Оновлення даних на сторінці
 */
function updatePageData(data) {
    // Оновлення статистики
    if (data.stats) {
        updateStatistics(data.stats);
    }
    
    // Оновлення датчиків
    if (data.sensors) {
        updateSensors(data.sensors);
    }
    
    // Оновлення графіків
    if (data.charts) {
        updateCharts(data.charts);
    }
}

/**
 * Оновлення статистичних показників
 */
function updateStatistics(stats) {
    // Знаходимо всі елементи для оновлення
    const statElements = document.querySelectorAll('[data-stat]');
    
    statElements.forEach(element => {
        const statName = element.getAttribute('data-stat');
        if (stats[statName] !== undefined) {
            // Анімація зміни значення
            const oldValue = parseFloat(element.textContent) || 0;
            const newValue = parseFloat(stats[statName]) || 0;
            
            animateValue(element, oldValue, newValue, 1000);
        }
    });
}

/**
 * Анімація зміни числового значення
 */
function animateValue(element, start, end, duration) {
    let startTimestamp = null;
    const step = (timestamp) => {
        if (!startTimestamp) startTimestamp = timestamp;
        const progress = Math.min((timestamp - startTimestamp) / duration, 1);
        const value = Math.floor(progress * (end - start) + start);
        element.textContent = value;
        if (progress < 1) {
            window.requestAnimationFrame(step);
        } else {
            element.textContent = end;
        }
    };
    window.requestAnimationFrame(step);
}

/**
 * Налаштування живого пошуку
 */
function setupLiveSearch() {
    const searchInput = document.getElementById('searchInput');
    
    if (searchInput) {
        searchInput.addEventListener('input', function() {
            const searchTerm = this.value.toLowerCase().trim();
            
            // Знаходимо елементи для фільтрації (наприклад, картки датчиків)
            const searchableItems = document.querySelectorAll('.searchable-item, .sensor-item');
            
            // Якщо елементів немає, виходимо
            if (searchableItems.length === 0) return;
            
            // Підсвічування знайденого тексту
            searchableItems.forEach(function(item) {
                // Скидаємо попередні підсвічування
                item.querySelectorAll('.highlight-text').forEach(el => {
                    el.outerHTML = el.textContent;
                });
                
                const itemText = item.textContent.toLowerCase();
                const itemType = item.getAttribute('data-type') || '';
                
                if ((searchTerm === '') || itemText.includes(searchTerm) || itemType.includes(searchTerm)) {
                    item.style.display = '';
                    
                    // Підсвічування тексту, якщо пошуковий термін не порожній
                    if (searchTerm !== '') {
                        highlightText(item, searchTerm);
                    }
                } else {
                    item.style.display = 'none';
                }
            });
            
            // Оновлення лічильника видимих елементів
            const visibleCount = document.querySelectorAll('.searchable-item:not([style*="display: none"]), .sensor-item:not([style*="display: none"])').length;
            const totalCount = searchableItems.length;
            const searchResultCounter = document.getElementById('searchResultCounter');
            
            if (searchResultCounter) {
                searchResultCounter.textContent = `${visibleCount} з ${totalCount}`;
            }
        });
        
        // Додаємо очищення пошуку по Escape
        searchInput.addEventListener('keydown', function(e) {
            if (e.key === 'Escape') {
                this.value = '';
                this.dispatchEvent(new Event('input'));
                this.blur();
            }
        });
        
        // Додаємо кнопку очищення пошуку
        const searchContainer = searchInput.parentElement;
        if (searchContainer) {
            const clearButton = document.createElement('button');
            clearButton.className = 'btn btn-outline-secondary clear-search';
            clearButton.innerHTML = '<i class="fas fa-times"></i>';
            clearButton.title = 'Очистити пошук';
            clearButton.style.display = 'none';
            
            clearButton.addEventListener('click', function() {
                searchInput.value = '';
                searchInput.dispatchEvent(new Event('input'));
                searchInput.focus();
                this.style.display = 'none';
            });
            
            searchContainer.appendChild(clearButton);
            
            // Показуємо/приховуємо кнопку очищення при введенні тексту
            searchInput.addEventListener('input', function() {
                clearButton.style.display = this.value ? 'block' : 'none';
            });
        }
    }
}

/**
 * Підсвічування тексту в елементі
 */
function highlightText(element, searchText) {
    const nodes = [...element.childNodes];
    
    nodes.forEach(node => {
        if (node.nodeType === Node.TEXT_NODE) {
            const text = node.textContent;
            const lowerText = text.toLowerCase();
            const index = lowerText.indexOf(searchText.toLowerCase());
            
            if (index >= 0) {
                const before = text.substring(0, index);
                const match = text.substring(index, index + searchText.length);
                const after = text.substring(index + searchText.length);
                
                const span = document.createElement('span');
                span.className = 'highlight-text';
                span.textContent = match;
                span.style.backgroundColor = 'rgba(255, 255, 0, 0.3)';
                span.style.padding = '0 2px';
                span.style.borderRadius = '2px';
                
                const fragment = document.createDocumentFragment();
                if (before) fragment.appendChild(document.createTextNode(before));
                fragment.appendChild(span);
                if (after) fragment.appendChild(document.createTextNode(after));
                
                node.parentNode.replaceChild(fragment, node);
            }
        } else if (node.nodeType === Node.ELEMENT_NODE && !node.classList.contains('highlight-text')) {
            highlightText(node, searchText);
        }
    });
}

/**
 * Налаштування перемикачів періодів для графіків
 */
function setupTimeRangeSwitchers() {
    const timeRangeButtons = document.querySelectorAll('.time-range-btn');
    
    timeRangeButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            
            // Зняття активного класу з усіх кнопок
            timeRangeButtons.forEach(btn => btn.classList.remove('active'));
            
            // Додавання активного класу до натиснутої кнопки
            this.classList.add('active');
            
            // Додавання анімації клікання
            this.classList.add('clicked');
            setTimeout(() => {
                this.classList.remove('clicked');
            }, 300);
            
            // Отримання обраного періоду
            const range = this.getAttribute('data-range');
            
            // Показ спіннера завантаження
            const chartContainer = document.querySelector('.active-chart-container');
            if (chartContainer) {
                chartContainer.classList.add('loading');
                
                const loadingSpinner = document.createElement('div');
                loadingSpinner.className = 'chart-loading-spinner';
                loadingSpinner.innerHTML = '<div class="spinner-border text-primary" role="status"><span class="visually-hidden">Завантаження...</span></div>';
                
                chartContainer.appendChild(loadingSpinner);
            }
            
            // Оновлення графіків відповідно до обраного періоду
            updateChartsForTimeRange(range);
        });
    });
}

/**
 * Оновлення графіків на основі обраного часового періоду
 */
function updateChartsForTimeRange(range) {
    const chartId = document.querySelector('.active-chart-container')?.getAttribute('data-chart-id');
    const sensorId = document.getElementById('sensorId')?.value;
    
    if (!chartId || !sensorId) return;
    
    // Показ спінера завантаження
    const loadingSpinner = document.getElementById(`${chartId}Loading`);
    if (loadingSpinner) {
        loadingSpinner.classList.remove('d-none');
    }
    
    // AJAX запит для отримання даних за обраний період
    fetch(`/readings/sensor/${sensorId}/range?range=${range}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Помилка отримання даних');
            }
            return response.json();
        })
        .then(data => {
            // Оновлення графіка з новими даними
            updateChart(chartId, data);
            
            // Оновлення підпису періоду
            updateTimeRangeLabel(range);
            
            // Анімація оновлення графіка
            const chartContainer = document.querySelector('.active-chart-container');
            if (chartContainer) {
                chartContainer.classList.add('updated');
                setTimeout(() => {
                    chartContainer.classList.remove('updated');
                }, 1000);
            }
        })
        .catch(error => {
            console.error('Помилка отримання даних за період:', error);
            showToast('Помилка отримання даних за обраний період: ' + error.message, 'danger');
        })
        .finally(() => {
            // Приховання спінера завантаження
            if (loadingSpinner) {
                loadingSpinner.classList.add('d-none');
            }
            
            // Видалення спіннера завантаження з контейнера графіка
            const chartContainer = document.querySelector('.active-chart-container');
            if (chartContainer) {
                chartContainer.classList.remove('loading');
                const spinners = chartContainer.querySelectorAll('.chart-loading-spinner');
                spinners.forEach(spinner => spinner.remove());
            }
        });
}

/**
 * Оновлення підпису періоду
 */
function updateTimeRangeLabel(range) {
    const timeRangeLabel = document.getElementById('currentTimeRange');
    
    if (timeRangeLabel) {
        let labelText = 'Останні дані за: ';
        
        switch (range) {
            case 'day':
                labelText += 'сьогодні';
                break;
            case 'week':
                labelText += 'тиждень';
                break;
            case 'month':
                labelText += 'місяць';
                break;
            case 'year':
                labelText += 'рік';
                break;
            default:
                labelText += '24 години';
        }
        
        // Анімація зміни тексту
        timeRangeLabel.style.opacity = '0';
        setTimeout(() => {
            timeRangeLabel.textContent = labelText;
            timeRangeLabel.style.opacity = '1';
        }, 300);
    }
}

/**
 * Оновлення даних на графіку
 */
function updateChart(chartId, data) {
    const chart = Chart.getChart(chartId);
    
    if (chart) {
        // Анімація оновлення даних
        chart.data.labels = data.labels;
        chart.data.datasets[0].data = data.values;
        
        // Налаштування анімації
        chart.options.animation = {
            duration: 800,
            easing: 'easeOutQuart'
        };
        
        chart.update();
    }
}

/**
 * Налаштування сповіщень (toasts)
 */
function setupToasts() {
    // Перевірка наявності контейнера для тостів
    let toastContainer = document.querySelector('.toast-container');
    
    // Якщо контейнер не існує, створюємо його
    if (!toastContainer) {
        toastContainer = document.createElement('div');
        toastContainer.className = 'toast-container position-fixed bottom-0 end-0 p-3';
        document.body.appendChild(toastContainer);
    }
}

/**
 * Відображення повідомлення (toast)
 */
function showToast(message, type = 'info') {
    // Перевірка наявності контейнера для тостів
    let toastContainer = document.querySelector('.toast-container');
    
    // Якщо контейнер не існує, створюємо його
    if (!toastContainer) {
        toastContainer = document.createElement('div');
        toastContainer.className = 'toast-container position-fixed bottom-0 end-0 p-3';
        document.body.appendChild(toastContainer);
    }
    
    // Генерація унікального ID для тосту
    const toastId = 'toast-' + new Date().getTime();
    
    // Визначення іконки залежно від типу повідомлення
    let icon;
    switch (type) {
        case 'success':
            icon = 'check-circle';
            break;
        case 'danger':
        case 'error':
            icon = 'exclamation-circle';
            type = 'danger';
            break;
        case 'warning':
            icon = 'exclamation-triangle';
            break;
        case 'info':
        default:
            icon = 'info-circle';
            break;
    }
    
    // Створення HTML для тосту
    const toastHTML = `
        <div id="${toastId}" class="toast align-items-center text-white bg-${type} border-0" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="d-flex">
                <div class="toast-body">
                    <i class="fas fa-${icon} me-2"></i>
                    ${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Закрити"></button>
            </div>
        </div>
    `;
    
    // Додавання тосту до контейнера
    toastContainer.insertAdjacentHTML('beforeend', toastHTML);
    
    // Отримання DOM-елемента тосту
    const toastElement = document.getElementById(toastId);
    
    // Ініціалізація тосту
    const toast = new bootstrap.Toast(toastElement, {
        animation: true,
        autohide: true,
        delay: 5000
    });
    
    // Показ тосту
    toast.show();
    
    // Автоматичне видалення елемента після закриття
    toastElement.addEventListener('hidden.bs.toast', function() {
        toastElement.remove();
    });
    
    // Додавання анімації появи
    toastElement.style.transform = 'translateY(20px)';
    toastElement.style.opacity = '0';
    
    setTimeout(() => {
        toastElement.style.transition = 'transform 0.3s ease, opacity 0.3s ease';
        toastElement.style.transform = 'translateY(0)';
        toastElement.style.opacity = '1';
    }, 10);
    
    return toast;
}

/**
 * Налаштування плавного прокручування до якорів
 */
function setupSmoothScrolling() {
    // Знаходимо всі посилання-якоря
    const anchorLinks = document.querySelectorAll('a[href^="#"]');
    
    anchorLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            // Отримуємо ID цільового елемента
            const targetId = this.getAttribute('href');
            
            // Перевіряємо, чи не порожній ID і чи існує елемент з таким ID
            if (targetId !== '#' && document.querySelector(targetId)) {
                e.preventDefault();
                
                // Отримуємо цільовий елемент
                const targetElement = document.querySelector(targetId);
                
                // Прокручуємо до елемента з плавною анімацією
                targetElement.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
                
                // Додаємо історію браузера для можливості навігації назад
                history.pushState(null, null, targetId);
                
                // Підсвічування цільового елемента
                targetElement.classList.add('highlight');
                setTimeout(() => {
                    targetElement.classList.remove('highlight');
                }, 2000);
            }
        });
    });
}
