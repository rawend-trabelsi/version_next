// src/app/reset-password/page.jsx
'use client';  // Important: ceci marque le composant comme un Client Component
import { useState } from 'react';
import { useRouter } from 'next/navigation';

export default function ResetPassword() {
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState(''); // État pour le mot de passe de confirmation
  const [error, setError] = useState(''); // État pour les messages d'erreur
  const [successMessage, setSuccessMessage] = useState(''); // État pour le message de succès
  const router = useRouter();

  const handleResetPassword = () => {
    setError(''); // Réinitialiser le message d'erreur à chaque tentative de réinitialisation
    setSuccessMessage(''); // Réinitialiser le message de succès

    if (!newPassword || !confirmPassword) {
      setError('Please enter a new password and confirm it.'); // Met à jour le message d'erreur
      return;
    }

    if (newPassword.length < 8) {
      setError('Password must be 8 characters.'); // Met à jour le message d'erreur pour le mot de passe
      return;
    }

    if (newPassword !== confirmPassword) {
      setError('Passwords do not match. Please try again.'); // Met à jour le message d'erreur pour les mots de passe non correspondants
      return;
    }

    // Simule la mise à jour du mot de passe
    const user = JSON.parse(localStorage.getItem('user'));
    if (user) {
      user.password = newPassword; // Met à jour le mot de passe
      localStorage.setItem('user', JSON.stringify(user));
      setSuccessMessage('Your password has been reset successfully!'); // Met à jour le message de succès
      setTimeout(() => {
        router.push('/pwd-reset-successfully'); // Redirige vers la page de connexion après un délai
      }, 2000); // Délai de 2 secondes
    }
  };

  return (
    <div className="flex items-center justify-center h-screen bg-white">
      <div className="bg-white p-8 rounded-lg w-full max-w-2xl">
        <h1 className="text-3xl font-semibold text-center mb-10">Set New Password</h1>

        {successMessage && (
          <div className="mb-4 p-3 text-black bg-[#B8EBC8] rounded-lg text-center">
            {successMessage}
          </div>
        )}
        
        <div className="mb-4">
          <label htmlFor="newPassword" className="block text-sm font-medium text-gray-700 mb-2"> 
            New Password
          </label>
          <input
            type="password"
            id="newPassword"
            className="w-full p-3 border border-gray-300 rounded-lg"
            placeholder='Enter new password'
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
            required
          />
          {newPassword.length < 8 && newPassword.length > 0 && (
            <p className="text-red-500 text-sm">Password must be 8 characters.</p>
          )}
          {newPassword.length >= 8 && (
            <p className="text-green-500 text-sm">Password length is valid.</p>
          )}
        </div>

        <div className="mb-4">
          <label htmlFor="confirmPassword" className="block text-sm font-medium text-gray-700 mb-2"> 
            Confirm Password
          </label>
          <input
            type="password"
            id="confirmPassword"
            className="w-full p-3 border border-gray-300 rounded-lg"
            placeholder='Confirm new password'
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            required
          />
          {confirmPassword.length > 0 && newPassword !== confirmPassword && (
            <p className="text-red-500 text-sm">Passwords do not match.</p>
          )}
          {confirmPassword.length > 0 && newPassword === confirmPassword && (
            <p className="text-green-500 text-sm">Passwords match.</p>
          )}
        </div>

        <button
          type="button"
          onClick={handleResetPassword}
          className="w-full bg-black text-white p-3 rounded-lg"
        >
          Reset Password
        </button>

        {error && (
          <p className="text-red-500 text-sm text-center mt-4">{error}</p>
        )}
      </div>
    </div>
  );
}