import ButtonWrapper from './components/ButtonWrapper';
import LoginForm from './components/LoginForm';

export default function Home() {
  return (
      <div className="flex items-center justify-center min-h-screen bg-gradient-to-br from-blue-100 to-purple-200 p-4 font-sans">
        <LoginForm />
    </div>
  );
}