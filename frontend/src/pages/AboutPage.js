import React, { useState, useEffect } from 'react';
import ReactTypingEffect from 'react-typing-effect';

function AboutPage() {
    const [currentTime, setCurrentTime] = useState('Загрузка...');
    const [isLoading, setIsLoading] = useState(false);

    const updateTimeFromServer = async () => {
        setIsLoading(true);
        setCurrentTime('Загрузка...');
        try {
            const response = await fetch('/api/current-time');
            if (!response.ok) {
                throw new Error('Ошибка при получении времени с сервера');
            }
            const data = await response.json();
            setCurrentTime(data.time);
        } catch (error) {
            setCurrentTime('Ошибка получения времени');
            console.error(error);
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        updateTimeFromServer();
    }, []);

    return (
        <div>
            <h1>About Page</h1>
            <p>Текущее время: {currentTime}</p>
            <button
                className="btn btn-primary"
                onClick={updateTimeFromServer}
                disabled={isLoading}
            >
                {isLoading ? 'Загрузка...' : 'Обновить текущее время'}
            </button>

            <ReactTypingEffect
                text={"   Добро пожаловать в квест!"}
                speed={24}
                eraseDelay={1000000}
                typingDelay={1000}
                /*cursor={' '}*/
                displayTextRenderer={(text, i) => {
                    return (
                        <h6>{"   " + text}</h6>
                    );
                }}
            />
        </div>
    );
}

export default AboutPage;
