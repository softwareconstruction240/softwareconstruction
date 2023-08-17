function submit() {
  document.getElementById('response').value = '';
  const method = document.getElementById('method').value;
  const endpoint = document.getElementById('handleBox').value;
  const requestBody = document.getElementById('requestBox').value;
  const authToken = document.getElementById('authToken').value;

  if (endpoint && method) {
    send(endpoint, requestBody, method, authToken);
  }

  return false;
}

function send(path, params, method, authToken) {
  params = !!params ? params : undefined;
  let errStr = '';
  fetch(path, {
    method: method,
    body: params,
    headers: {
      Authorization: authToken,
      'Content-Type': 'application/json',
    },
  })
    .then((response) => {
      if (!response.ok) errStr = response.status + ': ' + response.statusText + '\n';
      return response.json();
    })
    .then((data) => {
      document.getElementById('authToken').value = data.authToken || authToken || 'none';
      document.getElementById('response').innerText = errStr + JSON.stringify(data, null, 2);
    })
    .catch((error) => {
      document.getElementById('response').innerText = error;
    });
}

function displayRequest(method, endpoint, request) {
  document.getElementById('method').value = method;
  document.getElementById('handleBox').value = endpoint;
  const body = request ? JSON.stringify(request, null, 2) : '';
  document.getElementById('requestBox').value = body;
}

function clearAll() {
  displayRequest('DELETE', '/db', null);
}
function register() {
  displayRequest('POST', '/user', { username: 'username', password: 'password', email: 'email' });
}
function login() {
  displayRequest('POST', '/session', { username: 'username', password: 'password' });
}
function logout() {
  displayRequest('DELETE', '/session', null);
}
function gamesList() {
  displayRequest('GET', '/game', null);
}
function createGame() {
  displayRequest('POST', '/game', { gameName: 'gameName' });
}
function joinGame() {
  displayRequest('PUT', '/game', { playerColor: 'WHITE/BLACK/empty', gameID: 0 });
}
