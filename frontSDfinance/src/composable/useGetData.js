import { ref } from 'vue'

export const useGetData = () => {
  const data = ref([])
  const isLoading = ref(false)
  const errorMessage = ref('')

  const getData = async (type) => {
    isLoading.value = true
    errorMessage.value = ''
    try {
      const response = await fetch('/articles.js')
      if (!response.ok) throw new Error('Erreur lors du chargement des données')
      const text = await response.text()
      // Évaluation du module articles.js – extraction selon le type demandé
      const evalModule = new Function(text + '\nreturn { membres, transactions, caisses, conversations }')
      const all = evalModule()
      if (!type) data.value = all
      else data.value = all[type] ?? []
    } catch (error) {
      errorMessage.value = 'Erreur de chargement des données'
      console.error(error)
    } finally {
      isLoading.value = false
    }
  }

  return { data, errorMessage, isLoading, getData }
}
