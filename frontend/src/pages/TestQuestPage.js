import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Button, Card, ListGroup, Alert, ProgressBar, Modal, Image } from 'react-bootstrap';
import ReactTypingEffect from 'react-typing-effect';

function TestQuestPage() {
    const [location, setLocation] = useState(null); // Текущая локация
    const [inventory, setInventory] = useState([]); // Инвентарь игрока
    const [neighbors, setNeighbors] = useState([]); // Соседние локации
    const [locationItems, setLocationItems] = useState([]); // Предметы в текущей локации
    const [actions, setActions] = useState([]); // Доступные действия
    const [message, setMessage] = useState(''); // Служебные сообщения (например, ошибки)
    const [effectMessage, setEffectMessage] = useState(''); // Сообщения об эффектах
    const [health, setHealth] = useState(100); // Здоровье игрока
    const [showMapModal, setShowMapModal] = useState(false); // Отображение модального окна с картой

    // Получение данных с сервера
    const fetchQuestData = async () => {
        try {
            const response = await fetch('/api/test-quest');
            const data = await response.json();
            if (!response.ok) {
                throw new Error(data.error || `Ошибка: ${response.status}`);
            }
            setLocation(data.location || null); // Устанавливаем локацию
            setInventory(data.inventory || []); // Устанавливаем инвентарь
            setNeighbors(data.neighbors || []); // Устанавливаем соседей
            setLocationItems(data.locationItems || []); // Устанавливаем предметы в локации
            setActions(data.actions || []); // Устанавливаем доступные действия
            if (data.health !== undefined) {
                setHealth(data.health); // Обновляем здоровье игрока
            } else {
                setHealth(100); // Значение по умолчанию
            }
        } catch (error) {
            console.error('Ошибка при получении данных квеста', error);
            setMessage('Ошибка при получении данных квеста');
        }
    };

    // Отправка POST-запроса
    const handleAction = async (actionDescription, itemId = null) => {
        try {
            const bodyData = itemId
                ? { action: actionDescription, itemId: itemId }
                : { action: actionDescription };
            const currentNeighbors = [...neighbors];
            const response = await fetch('/api/test-quest', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(bodyData), // Отправка данных
            });
            const data = await response.json();
            if (!response.ok) {
                throw new Error(data.error || `Ошибка: ${response.status}`);
            }
            // Проверка, является ли действие перемещением
            const isMovement = actionDescription === 'move';
            fetchQuestData(); // Обновляем данные после действия
            if (!isMovement) {
                setEffectMessage(data.message || 'Действие успешно выполнено.');
            } else {
                setEffectMessage(''); // Очистка effectMessage при перемещении
            }
        } catch (error) {
            console.error('Ошибка при выполнении действия', error);
            setMessage(error.message);
        }
    };

    useEffect(() => {
        fetchQuestData();
    }, []);

    // Перенаправление
    useEffect(() => {
        if (message === 'Вы не авторизованы') {
            window.location.href = '/login';
        }
    }, [message]);

    return (
        <Container className="mt-4">
            {/* Заголовок */}
            <Row>
                <Col style={{ height: '120px' }}>
                    <h1 className="text-center custom-heading2">"Квест: «Параллакс»"</h1>
                </Col>
            </Row>

            <Row>
                {/* Левая колонка */}
                <Col>
                    <Row>
                        {location ? (
                            <ReactTypingEffect
                                key={location.id}
                                text={location.id}
                                speed={50}
                                eraseDelay={1000000}
                                eraseSpeed={0}
                                typingDelay={500}
                                cursor={' '}
                                displayTextRenderer={(text) => (
                                    <h2 className="text-center mb-3 custom-heading2">{'Локация:  ' + text}</h2>
                                )}
                            />
                        ) : (
                            <h2 className="text-center">Загрузка...</h2>
                        )}
                    </Row>

                    <Row>
                        {location ? (
                            <ReactTypingEffect
                                key={location.description}
                                text={location.description}
                                speed={3}
                                eraseDelay={1000000}
                                eraseSpeed={0}
                                typingDelay={1400}
                                displayTextRenderer={(text) => (
                                    <div className="mx-auto text-left custom-heading" style={{ maxWidth: '1000px' }}>
                                        <p>{text}</p>
                                    </div>
                                )}
                            />
                        ) : (
                            <p className="text-center">Описание локации загружается...</p>
                        )}
                    </Row>

                    {/* Сообщение об эффектах */}
                    <Row className="mt-4">
                        <Col>
                            {effectMessage ? (
                                <ReactTypingEffect
                                    key={effectMessage}
                                    text={effectMessage}
                                    speed={3}
                                    eraseDelay={1000000}
                                    eraseSpeed={0}
                                    typingDelay={1400}
                                    displayTextRenderer={(text) => (
                                        <div className="mx-auto text-left custom-heading" style={{ maxWidth: '1000px' }}>
                                            <p>{text}</p>
                                        </div>
                                    )}
                                />
                            ) : (
                                <div style={{ minHeight: '50px' }}></div>
                            )}
                        </Col>
                    </Row>
                </Col>

                {/* Правая колонка */}
                <Col md={4}>
                    <Row>
                        <Col>
                            <Card className="mb-4">
                                <Card.Header>Здоровье</Card.Header>
                                <Card.Body>
                                    <ProgressBar now={health} label={`${health}%`} />
                                </Card.Body>
                            </Card>
                            <Card className="mb-4">
                                <Card.Header>Инвентарь</Card.Header>
                                <ListGroup variant="flush">
                                    {inventory && inventory.length > 0 ? (
                                        inventory.map((itemId, index) => (
                                            <ListGroup.Item key={index}>{itemId}</ListGroup.Item>
                                        ))
                                    ) : (
                                        <ListGroup.Item>Ваш инвентарь пуст.</ListGroup.Item>
                                    )}
                                </ListGroup>
                            </Card>
                            <Card className="mb-4">
                                <Card.Header>Предметы в локации</Card.Header>
                                <ListGroup variant="flush">
                                    {locationItems && locationItems.length > 0 ? (
                                        locationItems.map((item, index) => (
                                            <ListGroup.Item key={index}>
                                                <strong>{item.id}</strong>: {item.description}
                                                <Button
                                                    variant="success"
                                                    size="sm"
                                                    className="float-right"
                                                    onClick={() => handleAction('pick_up', item.id)}
                                                >
                                                    Подобрать
                                                </Button>
                                            </ListGroup.Item>
                                        ))
                                    ) : (
                                        <ListGroup.Item>В этой локации нет предметов.</ListGroup.Item>
                                    )}
                                </ListGroup>
                            </Card>
                            <Card className="mb-4">
                                <Card.Header>Действия</Card.Header>
                                <Card.Body>
                                    {actions && actions.length > 0 ? (
                                        actions.map((action, index) => (
                                            <Button
                                                key={index}
                                                variant="warning"
                                                className="m-1"
                                                onClick={() => handleAction(action.description)}
                                            >
                                                {action.description}
                                            </Button>
                                        ))
                                    ) : (
                                        <p>Нет доступных действий.</p>
                                    )}
                                </Card.Body>
                            </Card>
                            <Card className="mb-4">
                                <Card.Header>Переходы</Card.Header>
                                <Card.Body>
                                    {neighbors && neighbors.length > 0 ? (
                                        neighbors.map((neighbor, index) => (
                                            <Button
                                                key={index}
                                                variant="primary"
                                                className="m-1"
                                                onClick={() => handleAction('move', neighbor.id)}
                                            >
                                                Переход к {neighbor.id}
                                            </Button>
                                        ))
                                    ) : (
                                        <p>Нет доступных переходов.</p>
                                    )}
                                </Card.Body>
                            </Card>
                            <Button
                                variant="info"
                                className="mb-4 w-100"
                                onClick={() => setShowMapModal(true)}
                            >
                                Показать карту
                            </Button>
                        </Col>
                    </Row>
                </Col>
            </Row>
            {message && (
                <Row className="mt-4">
                    <Col>
                        <Alert variant="danger" onClose={() => setMessage('')} dismissible>
                            {message}
                        </Alert>
                    </Col>
                </Row>
            )}
            <Modal show={showMapModal} onHide={() => setShowMapModal(false)} size="lg" centered>
                <Modal.Header closeButton>
                    <Modal.Title>Карта квеста</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Image
                        src="/images/map2.png"
                        alt="Карта квеста"
                        fluid
                    />
                </Modal.Body>
            </Modal>
        </Container>
    );
}

export default TestQuestPage;
