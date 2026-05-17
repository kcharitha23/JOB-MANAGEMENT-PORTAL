document.addEventListener('DOMContentLoaded', function() {
    const bubble = document.getElementById('chatbot-bubble');
    const window = document.getElementById('chatbot-window');
    const closeBtn = document.getElementById('chatbot-close');
    const input = document.getElementById('chatbot-input');
    const sendBtn = document.getElementById('chatbot-send');
    const messages = document.getElementById('chatbot-messages');

    // Toggle Chat Window
    bubble.addEventListener('click', () => {
        window.style.display = 'flex';
        bubble.style.opacity = '0';
        bubble.style.pointerEvents = 'none';
        
        if (messages.children.length === 0) {
            setTimeout(() => {
                addBotMessage("Hi! I'm CareerBuddy, your AI career assistant. Are you looking for a job or looking to hire?", ["I'm looking for a job", "I want to hire someone"]);
            }, 500);
        }
    });

    closeBtn.addEventListener('click', () => {
        window.style.display = 'none';
        bubble.style.opacity = '1';
        bubble.style.pointerEvents = 'auto';
    });

    // Send Message
    const sendMessage = async () => {
        const text = input.value.trim();
        if (!text) return;

        addUserMessage(text);
        input.value = '';

        // Show typing indicator
        const typingId = addTypingIndicator();

        try {
            const response = await fetch('/api/chat', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ message: text })
            });
            
            if (!response.ok) throw new Error('API Error');
            
            const data = await response.json();
            
            // Artificial delay for realism
            setTimeout(() => {
                removeTypingIndicator(typingId);
                addBotMessage(data.message, data.suggestions);
            }, 1000);
            
        } catch (error) {
            setTimeout(() => {
                removeTypingIndicator(typingId);
                addBotMessage("Oops! I'm having trouble connecting to my brain. Please try again later.");
            }, 1000);
        }
    };

    sendBtn.addEventListener('click', sendMessage);
    input.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') sendMessage();
    });

    function addUserMessage(text) {
        const div = document.createElement('div');
        div.className = 'chat-message user animate-in';
        div.textContent = text;
        messages.appendChild(div);
        scrollToBottom();
    }

    function addBotMessage(text, suggestions = []) {
        const container = document.createElement('div');
        container.className = 'bot-message-container animate-in';
        
        const div = document.createElement('div');
        div.className = 'chat-message bot';
        div.textContent = text;
        container.appendChild(div);

        if (suggestions.length > 0) {
            const suggestDiv = document.createElement('div');
            suggestDiv.className = 'chat-suggestions';
            suggestions.forEach(s => {
                const chip = document.createElement('div');
                chip.className = 'suggestion-chip';
                chip.textContent = s;
                chip.onclick = () => {
                    input.value = s;
                    sendMessage();
                };
                suggestDiv.appendChild(chip);
            });
            container.appendChild(suggestDiv);
        }

        messages.appendChild(container);
        scrollToBottom();
    }

    function addTypingIndicator() {
        const id = 'typing-' + Date.now();
        const div = document.createElement('div');
        div.id = id;
        div.className = 'chat-message bot typing animate-in';
        div.innerHTML = '<div class="typing-dots"><span></span><span></span><span></span></div>';
        messages.appendChild(div);
        scrollToBottom();
        return id;
    }

    function removeTypingIndicator(id) {
        const el = document.getElementById(id);
        if (el) el.remove();
    }

    function scrollToBottom() {
        messages.scrollTop = messages.scrollHeight;
    }
});
