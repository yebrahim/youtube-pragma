const axios = require('axios');

function quick() {
  console.log('quick done', Date.now());
}

async function io() {
  await axios('http://msn.com');
  console.log('io done', Date.now());
}

function fibonacci(n) {
  if (n < 2) {
    return n;
  }
  return fibonacci(n - 1) + fibonacci(n - 2);
}

const q = [];
for (let i = 0; i < 10; ++i) {
  q.push(() => quick());
  q.push(async () => await io());
  q.push(() => fibonacci(40))
}

(async () => {
  const start = Date.now();
  await Promise.all(q.map(el => el()));
  for (let i = 0; i < q.length; ++i) {
    await q[i]();
  }
  console.log('done in', Date.now() - start)
})();
