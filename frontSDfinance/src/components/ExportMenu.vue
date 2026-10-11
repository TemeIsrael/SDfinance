<template>
  <div class="export-menu-container">
    <button 
      type="button" 
      class="export-btn" 
      :class="[sizeClass]" 
      @click="toggleDropdown"
      :title="'Exporter ou Imprimer ' + title"
    >
      <span class="icon">📥</span>
      <span>{{ buttonText || 'Exporter' }}</span>
      <span class="arrow">▼</span>
    </button>

    <div v-if="isOpen" class="dropdown-menu" @click.stop>
      <button type="button" class="dropdown-item" @click="handleCsvExport">
        <span class="item-icon">📊</span>
        <div class="item-text">
          <span class="item-title">Format Excel / CSV</span>
          <span class="item-desc">Fichier .csv avec encodage UTF-8</span>
        </div>
      </button>

      <button type="button" class="dropdown-item" @click="handlePdfExport">
        <span class="item-icon">📄</span>
        <div class="item-text">
          <span class="item-title">Format PDF / Imprimer</span>
          <span class="item-desc">Rapport imprimable stylisé avec entête</span>
        </div>
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { exportToCSV, exportToPDF } from '@/utils/exportUtils.js'

const props = defineProps({
  title: {
    type: String,
    required: true
  },
  subtitle: {
    type: String,
    default: ''
  },
  filename: {
    type: String,
    default: 'export_sdfinance'
  },
  headers: {
    type: Array,
    required: true
  },
  rows: {
    type: Array,
    required: true
  },
  summary: {
    type: Array,
    default: () => []
  },
  buttonText: {
    type: String,
    default: 'Exporter'
  },
  size: {
    type: String,
    default: 'medium'
  }
})

const isOpen = ref(false)

const sizeClass = props.size === 'small' ? 'btn-small' : 'btn-medium'

function toggleDropdown(e) {
  e.stopPropagation()
  isOpen.value = !isOpen.value
}

function closeDropdown() {
  isOpen.value = false
}

function handleCsvExport() {
  closeDropdown()
  exportToCSV({
    filename: props.filename,
    headers: props.headers,
    rows: props.rows
  })
}

function handlePdfExport() {
  closeDropdown()
  exportToPDF({
    title: props.title,
    subtitle: props.subtitle,
    headers: props.headers,
    rows: props.rows,
    summary: props.summary
  })
}

function handleOutsideClick(e) {
  if (isOpen.value) {
    isOpen.value = false
  }
}

onMounted(() => {
  window.addEventListener('click', handleOutsideClick)
})

onUnmounted(() => {
  window.removeEventListener('click', handleOutsideClick)
})
</script>

<style scoped>
.export-menu-container {
  position: relative;
  display: inline-block;
}

.export-btn {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  background: var(--primary-dark, #1e3a8a);
  color: white;
  border: none;
  border-radius: var(--radius, 6px);
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
  font-family: inherit;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.15);
}

.export-btn:hover {
  background: #0f172a;
  transform: translateY(-1px);
}

.btn-small {
  padding: 4px 10px;
  font-size: 0.8rem;
}

.btn-medium {
  padding: 7px 14px;
  font-size: 0.88rem;
}

.arrow {
  font-size: 0.65rem;
  opacity: 0.8;
  margin-left: 2px;
}

.dropdown-menu {
  position: absolute;
  right: 0;
  top: calc(100% + 4px);
  z-index: 1000;
  background: white;
  min-width: 230px;
  border-radius: 8px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
  border: 1px solid #e2e8f0;
  overflow: hidden;
  animation: fadeIn 0.15s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-4px); }
  to { opacity: 1; transform: translateY(0); }
}

.dropdown-item {
  display: flex;
  align-items: center;
  gap: 10px;
  width: 100%;
  padding: 10px 14px;
  background: none;
  border: none;
  text-align: left;
  cursor: pointer;
  transition: background 0.15s ease;
  color: #1e293b;
}

.dropdown-item:hover {
  background: #f1f5f9;
}

.dropdown-item:not(:last-child) {
  border-bottom: 1px solid #f1f5f9;
}

.item-icon {
  font-size: 1.2rem;
}

.item-text {
  display: flex;
  flex-direction: column;
}

.item-title {
  font-weight: 600;
  font-size: 0.85rem;
  color: #0f172a;
}

.item-desc {
  font-size: 0.72rem;
  color: #64748b;
}
</style>
