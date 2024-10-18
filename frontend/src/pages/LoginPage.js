import React, { useState } from 'react';
import { Container, Form, Button, Alert } from 'react-bootstrap';
import { Link } from 'react-router-dom';

function LoginPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');
    const [variant, setVariant] = useState('info');

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch('/api/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username, password }),
            });
            const data = await response.json();
            if (!response.ok) {
                throw new Error(data.error || 'Ошибка авторизации');
            }
            setVariant('success');
            setMessage('Вход успешен! Переходим к квесту...');
            // Перенаправление на страницу квеста через 1 секунду
            setTimeout(() => {
                window.location.href = '/test-quest';
            }, 1000);
        } catch (error) {
            setVariant('danger');
            setMessage(error.message);
        }
    };

    return (
        <Container className="mt-4">
            <h2>Вход</h2>
            {message && (
                <Alert variant={variant} onClose={() => setMessage('')} dismissible>
                    {message}
                </Alert>
            )}
            <Form onSubmit={handleLogin}>
                <Form.Group controlId="formUsername">
                    <Form.Label>Имя пользователя</Form.Label>
                    <Form.Control
                        type="text"
                        placeholder="Введите имя пользователя"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </Form.Group>

                <Form.Group controlId="formPassword">
                    <Form.Label>Пароль</Form.Label>
                    <Form.Control
                        type="password"
                        placeholder="Введите пароль"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </Form.Group>

                <Button variant="primary" type="submit">
                    Войти
                </Button>
            </Form>
            <p>Нет аккаунта? <Link to="/register">Зарегистрируйтесь</Link></p>
        </Container>
    );
}

export default LoginPage;
