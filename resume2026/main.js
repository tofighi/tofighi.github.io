document.addEventListener('DOMContentLoaded', () => {
    initClocks();
    initNews();
    initWeather();
    initEffects();
});

// --- Analog Clocks ---
function initClocks() {
    function updateClocks() {
        const now = new Date();
        
        // Toronto (Eastern Time) - typically UTC-5 or UTC-4
        const torontoTime = new Date(now.toLocaleString("en-US", {timeZone: "America/Toronto"}));
        
        // San Francisco (Pacific Time) - typically UTC-8 or UTC-7
        const sfTime = new Date(now.toLocaleString("en-US", {timeZone: "America/Los_Angeles"}));

        drawClockHands('clock-toronto', torontoTime);
        drawClockHands('clock-sf', sfTime);
        
        requestAnimationFrame(updateClocks);
    }

    function drawClockHands(id, date) {
        const clock = document.getElementById(id);
        const hourHand = clock.querySelector('.hour');
        const minuteHand = clock.querySelector('.minute');
        const secondHand = clock.querySelector('.second');

        const seconds = date.getSeconds() + date.getMilliseconds() / 1000;
        const minutes = date.getMinutes() + seconds / 60;
        const hours = date.getHours() + minutes / 60;

        const secondsDeg = (seconds / 60) * 360;
        const minutesDeg = (minutes / 60) * 360;
        const hoursDeg = (hours / 12) * 360;

        secondHand.style.transform = `translateX(-50%) rotate(${secondsDeg}deg)`;
        minuteHand.style.transform = `translateX(-50%) rotate(${minutesDeg}deg)`;
        hourHand.style.transform = `translateX(-50%) rotate(${hoursDeg}deg)`;
    }

    updateClocks();
}

// --- News Feed (AJAX) ---
async function initNews() {
    const container = document.getElementById('news-container');
    // Using rss2json to fetch CBC Canada Top Stories
    const rssUrl = 'https://www.cbc.ca/cmlink/rss-canada';
    const apiUrl = `https://api.rss2json.com/v1/api.json?rss_url=${encodeURIComponent(rssUrl)}`;

    try {
        const response = await fetch(apiUrl);
        const data = await response.json();

        if (data.status === 'ok') {
            container.innerHTML = '';
            data.items.slice(0, 5).forEach(item => {
                const newsItem = document.createElement('div');
                newsItem.className = 'news-item';
                newsItem.innerHTML = `
                    <a href="${item.link}" target="_blank">
                        <strong>${item.title}</strong>
                        <div style="font-size: 0.75rem; color: #94a3b8; margin-top: 5px;">${new Date(item.pubDate).toLocaleTimeString()}</div>
                    </a>
                `;
                container.appendChild(newsItem);
            });
        } else {
            container.innerHTML = 'Could not load news feed.';
        }
    } catch (err) {
        console.error('News fetch error:', err);
        container.innerHTML = 'Error loading news.';
    }
}

// --- Weather Info ---
function initWeather() {
    const widget = document.getElementById('weather-widget');

    if (!navigator.geolocation) {
        widget.innerHTML = 'Geolocation not supported';
        return;
    }

    navigator.geolocation.getCurrentPosition(async (position) => {
        const { latitude, longitude } = position.coords;
        const apiUrl = `https://api.open-meteo.com/v1/forecast?latitude=${latitude}&longitude=${longitude}&current_weather=true`;

        try {
            const response = await fetch(apiUrl);
            const data = await response.json();
            const weather = data.current_weather;
            
            widget.innerHTML = `
                <div style="font-weight: 600; color: #fff;">${weather.temperature}°C</div>
                <div style="font-size: 0.8rem; color: #94a3b8;">Current Location</div>
                <div style="font-size: 0.7rem; margin-top: 2px;">Wind: ${weather.windspeed} km/h</div>
            `;
        } catch (err) {
            widget.innerHTML = 'Weather unavailable';
        }
    }, () => {
        widget.innerHTML = 'Location access denied';
    });
}

// --- Effects & Canvas ---
function initEffects() {
    const canvas = document.getElementById('canvas-effects');
    const ctx = canvas.getContext('2d');
    let particles = [];

    function resize() {
        canvas.width = window.innerWidth;
        canvas.height = window.innerHeight;
    }

    window.addEventListener('resize', resize);
    resize();

    class Particle {
        constructor(x, y, color) {
            this.x = x;
            this.y = y;
            this.color = color;
            this.size = Math.random() * 8 + 4;
            this.speedX = Math.random() * 6 - 3;
            this.speedY = Math.random() * -10 - 2;
            this.gravity = 0.3;
            this.opacity = 1;
        }
        update() {
            this.x += this.speedX;
            this.y += this.speedY;
            this.speedY += this.gravity;
            this.opacity -= 0.02;
        }
        draw() {
            ctx.globalAlpha = this.opacity;
            ctx.fillStyle = this.color;
            ctx.beginPath();
            ctx.arc(this.x, this.y, this.size, 0, Math.PI * 2);
            ctx.fill();
        }
    }

    function spawnConfetti(count) {
        const colors = ['#6366f1', '#ec4899', '#f59e0b', '#10b981', '#ffffff'];
        const x = window.innerWidth / 2;
        const y = window.innerHeight + 10;
        
        for (let i = 0; i < count; i++) {
            particles.push(new Particle(
                Math.random() * window.innerWidth, 
                window.innerHeight + 10, 
                colors[Math.floor(Math.random() * colors.length)]
            ));
        }
    }

    function animate() {
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        for (let i = 0; i < particles.length; i++) {
            particles[i].update();
            particles[i].draw();
            if (particles[i].opacity <= 0) {
                particles.splice(i, 1);
                i--;
            }
        }
        requestAnimationFrame(animate);
    }

    animate();

    const congratsBtn = document.getElementById('congrats-btn');
    const celebrateBtn = document.getElementById('celebrate-btn');

    congratsBtn.addEventListener('click', () => {
        spawnConfetti(50);
    });

    celebrateBtn.addEventListener('click', () => {
        // More explosive
        for(let i=0; i<3; i++) {
            setTimeout(() => spawnConfetti(40), i * 200);
        }
    });
}
