<template>
  <div>
    <RouterView />
  </div>
</template>

<script setup></script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap');

*, *::before, *::after {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: 'Poppins', sans-serif;
}

:root {
  --primary: #f5ac71;
  --primary-dark: #f1737e;
  --radius: 10px;
  --sidebar-w: 150px;
  --header-h: 60px;
}
</style>
