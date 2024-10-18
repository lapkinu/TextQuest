import React, { useState, useEffect } from 'react';
import {Container, Row, Col, Button, Card, ListGroup, Alert, ProgressBar, Modal, Image
} from 'react-bootstrap';
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

    // Функция для получения данных с сервера (GET запрос)
    const fetchQuestData = async () => {
        try {
            const response = await fetch('/api/test-quest');
            const data = await response.json();
            if (!response.ok) {
                throw new Error(data.error || `Ошибка: ${response.status}`);
            }
            setLocation(data.location); // Устанавливаем локацию
            setInventory(data.inventory); // Устанавливаем инвентарь
            setNeighbors(data.neighbors); // Устанавливаем соседей
            setLocationItems(data.locationItems); // Устанавливаем предметы в локации
            setActions(data.actions); // Устанавливаем доступные действия
            if (data.health !== undefined) {
                setHealth(data.health); // Обновляем здоровье игрока
            }
            // setMessage(''); // Сбрасываем сообщение
        } catch (error) {
            console.error('Ошибка при получении данных квеста', error);
            setMessage('Ошибка при получении данных квеста');
        }
    };

    // Функция для отправки POST запроса (для действий)
    const handleAction = async (actionDescription, itemId = null) => {
        try {
            const bodyData = itemId
                ? { action: actionDescription, itemId: itemId }
                : { action: actionDescription };

            // Сохраняем текущих соседей
            const currentNeighbors = [...neighbors];

            const response = await fetch('/api/test-quest', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(bodyData), // Отправляем данные как JSON
            });
            const data = await response.json();
            if (!response.ok) {
                throw new Error(data.error || `Ошибка: ${response.status}`);
            }

            // Проверяем, является ли действие перемещением
            const isMovement = currentNeighbors.some(neighbor => neighbor.id === actionDescription);

            fetchQuestData(); // Обновляем данные после действия

            if (!isMovement) {
                setEffectMessage(data.message || 'Действие успешно выполнено.');
            } else {
                setEffectMessage(''); // Очищаем effectMessage при перемещении
            }
        } catch (error) {
            console.error('Ошибка при выполнении действия', error);
            setMessage(error.message);
        }
    };

    // Загрузка данных при загрузке страницы
    useEffect(() => {
        fetchQuestData();
    }, []);

    // Перенаправление на страницу авторизации, если не авторизованы
    useEffect(() => {
        if (message === 'Вы не авторизованы') {
            window.location.href = '/login';
        }
    }, [message]);

    return (
        <Container className="mt-4">
            {/* Заголовок сверху */}
            <Row>
                <Col style={{ height: '120px' }}>
                    <h1 className="text-center custom-heading2">"Потайная комната невидимки"</h1>
                </Col>
            </Row>

            <Row>
                {/* Левая колонка для названия и описания */}
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

                    {/* Постоянная область для отображения сообщений об эффектах */}
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
                                // Можно оставить пустой блок или добавить заглушку
                                <div style={{ minHeight: '50px' }}></div>
                            )}
                        </Col>
                    </Row>
                </Col>

                {/* Правая колонка для здоровья, инвентаря, предметов, действий и переходов */}
                <Col md={4}>
                    <Row>
                        <Col>
                            {/* Карточка здоровья */}
                            <Card className="mb-4">
                                <Card.Header>Здоровье</Card.Header>
                                <Card.Body>
                                    <ProgressBar now={health} label={`${health}%`} />
                                </Card.Body>
                            </Card>

                            {/* Карточка инвентаря */}
                            <Card className="mb-4">
                                <Card.Header>Инвентарь</Card.Header>
                                <ListGroup variant="flush">
                                    {inventory.length > 0 ? (
                                        inventory.map((itemId, index) => (
                                            <ListGroup.Item key={index}>{itemId}</ListGroup.Item>
                                        ))
                                    ) : (
                                        <ListGroup.Item>Ваш инвентарь пуст.</ListGroup.Item>
                                    )}
                                </ListGroup>
                            </Card>

                            {/* Карточка предметов в локации */}
                            <Card className="mb-4">
                                <Card.Header>Предметы в локации</Card.Header>
                                <ListGroup variant="flush">
                                    {locationItems.length > 0 ? (
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

                            {/* Карточка действий */}
                            <Card className="mb-4">
                                <Card.Header>Действия</Card.Header>
                                <Card.Body>
                                    {actions.length > 0 ? (
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

                            {/* Карточка переходов */}
                            <Card className="mb-4">
                                <Card.Header>Переходы</Card.Header>
                                <Card.Body>
                                    {neighbors.length > 0 ? (
                                        neighbors.map((neighbor, index) => (
                                            <Button
                                                key={index}
                                                variant="primary"
                                                className="m-1"
                                                onClick={() => handleAction(neighbor.id)}
                                            >
                                                Переход к {neighbor.name}
                                            </Button>
                                        ))
                                    ) : (
                                        <p>Нет доступных переходов.</p>
                                    )}
                                </Card.Body>
                            </Card>

                            {/* Кнопка "Показать карту" */}
                            <Button
                                variant="info"
                                className="mb-4"
                                onClick={() => setShowMapModal(true)}
                                block
                            >
                                Показать карту
                            </Button>
                        </Col>
                    </Row>
                </Col>
            </Row>

            {/* Служебные сообщения (например, ошибки) */}
            {message && (
                <Row className="mt-4">
                    <Col>
                        <Alert variant="danger" onClose={() => setMessage('')} dismissible>
                            {message}
                        </Alert>
                    </Col>
                </Row>
            )}

            {/* Модальное окно для карты */}
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
