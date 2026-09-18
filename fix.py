import os

path = 'apps/apae/src/components/buttons/trashButton.tsx'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace('import { useRouter } from "next/navigation";', 'import { useRouter } from "next/navigation";\nimport { useState } from "react";')
content = content.replace('const router = useRouter();', 'const router = useRouter();\n  const [isLoading, setIsLoading] = useState(false);')

old_func = '''  const deletarAgendamento = async () => {
    await deleteAppointment(id);
    router.back();
  };'''

new_func = '''  const deletarAgendamento = async () => {
    if (isLoading) return;
    try {
      setIsLoading(true);
      await deleteAppointment(id);
      router.back();
    } catch (error) {
      console.error(error);
      setIsLoading(false);
    }
  };'''

content = content.replace(old_func, new_func)

content = content.replace('<Button onClick={deletarAgendamento} type="submit">', '<Button onClick={deletarAgendamento} type="submit" disabled={isLoading}>')
content = content.replace('Sim\n          </Button>', '{isLoading ? "Excluindo..." : "Sim"}\n          </Button>')

with open(path, 'w', encoding='utf-8') as f:
    f.write(content)

print('Fixed TrashButton.tsx')
