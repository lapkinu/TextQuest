import React from 'react';

function HomePage() {
    return (
        <div>
            <h1>Home Page <i className="fas fa-home"></i></h1>
            <p>Главная страница</p>
            <button className="btn btn-success">
                <i className="fas fa-thumbs-up"></i> Нажми меня
            </button>
        </div>
    );
}

export default HomePage;
