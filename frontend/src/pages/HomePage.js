import React from 'react';

function HomePage() {
    return (
        <div>
            <h1>Home Page <i className="fas fa-home"></i></h1> {/* Иконка на странице Home */}
            <p>Добро пожаловать на главную страницу!</p>
            <button className="btn btn-success">
                <i className="fas fa-thumbs-up"></i> Нажми меня {/* Иконка на кнопке */}
            </button>
        </div>
    );
}

export default HomePage;
