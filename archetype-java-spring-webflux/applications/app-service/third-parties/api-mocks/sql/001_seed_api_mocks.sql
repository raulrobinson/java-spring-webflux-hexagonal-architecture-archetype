INSERT INTO api_mocks (
    name,
    description,
    method,
    path,
    priority,
    enabled,
    source,
    request_headers,
    request_body,
    response_status,
    response_headers,
    response_body,
    created_at,
    updated_at
) VALUES (
    'users-list',
    'Mock GET /users seeded from third-parties SQL',
    'GET',
    '/users',
    10,
    TRUE,
    'script',
    NULL,
    NULL,
    200,
    '{"Content-Type":"application/json"}',
    '[{"id":1,"name":"Ada Lovelace"},{"id":2,"name":"Grace Hopper"}]',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

INSERT INTO api_mocks (
    name,
    description,
    method,
    path,
    priority,
    enabled,
    source,
    request_headers,
    request_body,
    response_status,
    response_headers,
    response_body,
    created_at,
    updated_at
) VALUES (
    'create-order',
    'Mock POST /orders with exact request header and body',
    'POST',
    '/orders',
    20,
    TRUE,
    'script',
    '{"Content-Type":"application/json","x-tenant-id":"demo"}',
    '{"productId":100,"quantity":2}',
    201,
    '{"Content-Type":"application/json","x-mock-source":"sql"}',
    '{"status":"created","orderId":"ORD-1000"}',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

