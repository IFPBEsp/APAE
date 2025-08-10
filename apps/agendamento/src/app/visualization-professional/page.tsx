'use client'

import { useState, useEffect, useRef } from 'react'
import { useRouter } from 'next/navigation'
import { 
  Search, 
  ChevronDown,
  MoreHorizontal,
  Edit, 
  Trash2, 
} from 'lucide-react'

import styles from './page.module.css'

interface Profissional {
  id: string
  nome: string
  documento: string
  area: string
  telefone: string
}

const mockData: Profissional[] = [
  {
    id: '1',
    nome: 'João',
    documento: 'CRN: 10644 / CRN-6',
    area: 'Nutrição',
    telefone: '(12) 91234-5678'
  },
  {
    id: '2',
    nome: 'Maria',
    documento: 'CREFITO: 87654 / SP',
    area: 'Fisioterapia',
    telefone: '(11) 98765-4321'
  },
  {
    id: '3',
    nome: 'Carlos',
    documento: 'CRM: 54321 / RJ',
    area: 'Clínico Geral',
    telefone: '(21) 91234-5678'
  },
  {
    id: '4',
    nome: 'Ana',
    documento: 'CRP: 06/12345',
    area: 'Psicologia',
    telefone: '(11) 95555-4444'
  },
  {
    id: '5',
    nome: 'Pedro',
    documento: 'COREN: 98765 / BA',
    area: 'Enfermagem',
    telefone: '(71) 93333-2222'
  }
]

export default function ProfissionaisPage() {
  const [profissionais, setProfissionais] = useState<Profissional[]>([])
  const [loading, setLoading] = useState(true)
  const [searchTerm, setSearchTerm] = useState('')
  const [areaFilter, setAreaFilter] = useState('all')
  const [showDeleteModal, setShowDeleteModal] = useState(false)
  const [selectedProfissional, setSelectedProfissional] = useState<Profissional | null>(null)
  const [activeActionsMenu, setActiveActionsMenu] = useState<string | null>(null)
  
  const router = useRouter()
  const actionsMenuRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
    setLoading(true)
    setTimeout(() => {
      setProfissionais(mockData)
      setLoading(false)
    }, 500)
  }, [])

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (actionsMenuRef.current && !actionsMenuRef.current.contains(event.target as Node)) {
        setActiveActionsMenu(null)
      }
    }
    document.addEventListener('mousedown', handleClickOutside)
    return () => {
      document.removeEventListener('mousedown', handleClickOutside)
    }
  }, [])

  const handleAddNew = () => {
    console.log('Navigate to add new professional page')
  }

  const handleEdit = (id: string) => {
    console.log(`Navigate to edit page for professional ${id}`)
    setActiveActionsMenu(null)
  }

  const openDeleteModal = (profissional: Profissional) => {
    setSelectedProfissional(profissional)
    setShowDeleteModal(true)
    setActiveActionsMenu(null)
  }

  const handleDeleteConfirm = () => {
    if (!selectedProfissional) return
    
    setProfissionais(prev => prev.filter(p => p.id !== selectedProfissional.id))
    setShowDeleteModal(false)
    setSelectedProfissional(null)
  }

  const toggleActionsMenu = (id: string) => {
    setActiveActionsMenu(prev => (prev === id ? null : id))
  }

  const filteredProfissionais = profissionais.filter(prof => {
    const matchesSearch = prof.nome.toLowerCase().includes(searchTerm.toLowerCase()) ||
                          prof.documento.toLowerCase().includes(searchTerm.toLowerCase())
    const matchesArea = areaFilter === 'all' || prof.area === areaFilter
    return matchesSearch && matchesArea
  })
  
  const uniqueAreas = ['all', ...Array.from(new Set(mockData.map(p => p.area)))]

  return (
    <div className={styles.pageContainer}>
      <div className={styles.contentWrapper}>
        
        {/* Header */}
        <header className={styles.header}>
          <h1 className={styles.title}>Profissionais da Saúde</h1>
          <button className={styles.primaryButton} onClick={handleAddNew}>
            Cadastrar Profissional
          </button>
        </header>

        {/* Controls */}
        <div className={styles.controls}>
          <div className={styles.searchWrapper}>
            <Search size={20} className={styles.searchIcon} />
            <input
              type="text"
              placeholder="Buscar profissional"
              className={styles.searchInput}
              value={searchTerm}
              onChange={e => setSearchTerm(e.target.value)}
            />
          </div>
          <div className={styles.selectWrapper}>
            <select 
              className={styles.areaSelect}
              value={areaFilter}
              onChange={e => setAreaFilter(e.target.value)}
            >
              {uniqueAreas.map(area => (
                <option key={area} value={area}>
                  {area === 'all' ? 'Área da Saúde' : area}
                </option>
              ))}
            </select>
            <ChevronDown size={16} className={styles.selectIcon} />
          </div>
        </div>

        {/* Table */}
        <div className={styles.tableContainer}>
          <table className={styles.table}>
            <thead>
              <tr>
                <th>Profissional</th>
                <th>Documento</th>
                <th>Área</th>
                <th>Telefone</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {loading ? (
                <tr>
                  <td colSpan={5} className={styles.loadingState}>Carregando...</td>
                </tr>
              ) : filteredProfissionais.length > 0 ? (
                filteredProfissionais.map((prof) => (
                  <tr key={prof.id}>
                    <td>{prof.nome}</td>
                    <td>{prof.documento}</td>
                    <td>{prof.area}</td>
                    <td>{prof.telefone}</td>
                    <td className={styles.actionsCell}>
                      <button 
                        className={styles.actionsButton} 
                        onClick={() => toggleActionsMenu(prof.id)}
                      >
                        <MoreHorizontal size={20} />
                      </button>
                      {activeActionsMenu === prof.id && (
                        <div className={styles.actionsMenu} ref={actionsMenuRef}>
                          <button onClick={() => handleEdit(prof.id)}>
                            <Edit size={14} /> Editar
                          </button>
                          <button onClick={() => openDeleteModal(prof)} className={styles.deleteOption}>
                            <Trash2 size={14} /> Excluir
                          </button>
                        </div>
                      )}
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan={5} className={styles.emptyState}>
                    Nenhum profissional encontrado.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Delete Confirmation Modal */}
      {showDeleteModal && selectedProfissional && (
        <div className={styles.modalOverlay}>
          <div className={styles.modal}>
            <h3>Excluir Profissional</h3>
            <p>
              Tem certeza que deseja excluir o profissional{' '}
              <strong>{selectedProfissional.nome}</strong>? Esta ação não pode ser desfeita.
            </p>
            <div className={styles.modalActions}>
              <button
                className={styles.secondaryButton}
                onClick={() => setShowDeleteModal(false)}
              >
                Cancelar
              </button>
              <button
                className={styles.dangerButton}
                onClick={handleDeleteConfirm}
              >
                Excluir
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}