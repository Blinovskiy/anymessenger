// const config = require('config');
// const config = require('../../config.json');
// const port = config.port;
// const ip = config.interface;

const port = 8081;
const ip = 'localhost';

// console.log("port", port);
// console.log("ip", ip);

export async function getAllMessages() {
  let response = await fetch(`http://${ip}:${port}/api/messages`);
  return await response.json();
}

export async function getLastNMessages(n: number) {
  let response = await fetch(`http://${ip}:${port}/api/messages/last/${n}`);
  return await response.json();
}

export async function postMessage(id, text, userId) {

  let response = await fetch(`http://${ip}:${port}/api/message`,
    {
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      method: "POST",
      body: JSON.stringify({
        "id": id,
        "text": text,
        "userId": userId
      })
    });

  return await response.json();
}

export async function deleteMessage(id) {

  let response = await fetch(`http://${ip}:${port}/api/message/delete/${id}`,
    {
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      method: "POST"
    });

  return await response.json();
}
