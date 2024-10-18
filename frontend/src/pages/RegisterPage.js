import React, { useState } from 'react';
import { Container, Form, Button, Alert } from 'react-bootstrap';
import { Link } from 'react-router-dom';

function RegisterPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');
    const [variant, setVariant] = useState('info');

    const handleRegister = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch('/api/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username, password }),
            });
            const data = await response.json();
            if (!response.ok) {
                throw new Error(data.error || 'Ошибка регистрации');
            }
            setVariant('success');
            setMessage('Регистрация успешна! Теперь вы можете войти.');
        } catch (error) {
            setVariant('danger');
            setMessage(error.message);
        }
    };

    return (
        <Container className="mt-4">
            <h2>Регистрация</h2>
            {message && (
                <Alert variant={variant} onClose={() => setMessage('')} dismissible>
                    {message}
                </Alert>
            )}
            <Form onSubmit={handleRegister}>
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
                    Зарегистрироваться
                </Button>
            </Form>
            <p>Уже есть аккаунт? <Link to="/login">Войдите</Link></p>
        </Container>
    );
}

export default RegisterPage;
