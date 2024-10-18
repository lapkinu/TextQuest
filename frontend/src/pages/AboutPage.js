import React, { useState, useEffect } from 'react';
import ReactTypingEffect from 'react-typing-effect';

function AboutPage() {
    const [currentTime, setCurrentTime] = useState('Загрузка...');
    const [isLoading, setIsLoading] = useState(false); // Добавляем состояние для отслеживания загрузки

    // Функция для обновления времени через API-запрос
    const updateTimeFromServer = async () => {
        setIsLoading(true); // Устанавливаем состояние загрузки перед началом запроса
        setCurrentTime('Загрузка...'); // Показываем "Загрузка..." при каждом новом запросе
        try {
            const response = await fetch('/api/current-time'); // Запрос к API
            if (!response.ok) {
                throw new Error('Ошибка при получении времени с сервера');
            }
            const data = await response.json();
            setCurrentTime(data.time); // Обновляем состояние с временем
        } catch (error) {
            setCurrentTime('Ошибка получения времени');
            console.error(error);
        } finally {
            setIsLoading(false); // Отключаем состояние загрузки после завершения запроса
        }
    };

    // Вызов обновления времени при загрузке компонента
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
                disabled={isLoading} // Отключаем кнопку на время загрузки, если нужно
            >
                {isLoading ? 'Загрузка...' : 'Обновить текущее время'}
            </button>

            <ReactTypingEffect
                text={"Добро пожаловать в наш квест!"}
                speed={24}                // Скорость печати
                eraseDelay={1000000}       // Предотвращаем удаление текста
                typingDelay={1000}           // Задержка перед началом печати
                /*cursor={' '}*/                // Убираем мигающий курсор после печати
                displayTextRenderer={(text, i) => {
                    return (
                        <h6>{text}</h6>     // Оборачиваем текст в заголовок h1
                    );
                }}
            />
        </div>
    );
}

export default AboutPage;
