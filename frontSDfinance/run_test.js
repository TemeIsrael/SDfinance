const puppeteer = require('puppeteer');

(async () => {
  const browser = await puppeteer.launch({ args: ['--no-sandbox'] });
  const page = await browser.newPage();
  page.on('console', msg => console.log('PAGE LOG:', msg.text()));
  page.on('pageerror', error => console.log('PAGE ERROR:', error.message, error.stack));
  
  await page.goto('http://localhost:5173');
  
  // Wait for the app to load
  await page.waitForTimeout(2000);
  
  // Find "Caisse de l'église" and click it
  const cards = await page.$$('.card-caisse');
  for (const card of cards) {
    const text = await page.evaluate(el => el.textContent, card);
    if (text.includes("Caisse de l'église")) {
      console.log("Clicking on Caisse de l'église");
      await card.click();
      break;
    }
  }
  
  await page.waitForTimeout(1000);
  await browser.close();
})();
