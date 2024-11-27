'use client';

import { useState, useEffect } from 'react';
import { useRouter, useSearchParams } from 'next/navigation'; // Pour récupérer les paramètres de l'URL

export default function EditProfile() {
  const router = useRouter();
  const searchParams = useSearchParams(); // Utilisez ceci pour récupérer les query params
  const [profile, setProfile] = useState(null);
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [role, setRole] = useState('User');
  const [status, setStatus] = useState('Active');
  const [loading, setLoading] = useState(true); // Indique si les données sont en cours de chargement

  useEffect(() => {
    // Récupération de l'email depuis les paramètres de l'URL
    const emailParam = searchParams.get('email');
    if (!emailParam) {
      router.push('/dashboard'); // Redirige si l'email n'est pas présent dans l'URL
      return;
    }

    // Récupération des profils dans localStorage
    const profiles = JSON.parse(localStorage.getItem('profiles')) || [];
    const foundProfile = profiles.find((profile) => profile.email === emailParam);

    if (foundProfile) {
      setProfile(foundProfile);
      setName(foundProfile.name);
      setEmail(foundProfile.email);
      setRole(foundProfile.role);
      setStatus(foundProfile.status);
    } else {
      router.push('/dashboard'); // Redirige si le profil n'est pas trouvé
    }

    setLoading(false); // Fin du chargement
  }, [searchParams, router]); // Recharger si searchParams change

  const handleSave = () => {
    const updatedProfile = { name, email, role, status };
    const profiles = JSON.parse(localStorage.getItem('profiles')) || [];
    const index = profiles.findIndex((profile) => profile.email === email);

    if (index !== -1) {
      profiles[index] = updatedProfile;
      localStorage.setItem('profiles', JSON.stringify(profiles));
    }

    router.push('/dashboard'); // Rediriger après sauvegarde
  };

  const handleCancel = () => {
    router.push('/dashboard'); // Rediriger si l'utilisateur annule l'édition
  };

  if (loading) {
    return <div>Loading...</div>; // Affiche un message de chargement
  }

  return (
    <div className="fixed inset-0 flex items-center justify-center bg-gray-500 bg-opacity-50">
      <div className="bg-white p-8 rounded-lg shadow-lg w-96">
        <h1 className="text-2xl font-semibold text-gray-800 mb-4 text-center">Edit Profile</h1>
        <form onSubmit={(e) => e.preventDefault()}>
          <div className="mb-4">
            <label htmlFor="name" className="block text-sm font-medium text-gray-700">Name</label>
            <input
              type="text"
              id="name"
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Enter Name"
            />
          </div>

          <div className="mb-4">
            <label htmlFor="email" className="block text-sm font-medium text-gray-700">Email</label>
            <input
              type="email"
              id="email"
              value={email}
              disabled
              className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <div className="mb-4">
            <label htmlFor="role" className="block text-sm font-medium text-gray-700">Role</label>
            <select
              id="role"
              value={role}
              onChange={(e) => setRole(e.target.value)}
              className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="User">User</option>
              <option value="Admin">Admin</option>
              <option value="Moderator">Moderator</option>
            </select>
          </div>

          <div className="mb-4">
            <label htmlFor="status" className="block text-sm font-medium text-gray-700">Status</label>
            <select
              id="status"
              value={status}
              onChange={(e) => setStatus(e.target.value)}
              className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="Active">Active</option>
              <option value="Inactive">Inactive</option>
            </select>
          </div>

          <div className="mt-6 flex justify-between space-x-4">
            <button
              onClick={handleCancel}
              className="w-full bg-gray-300 text-gray-700 p-3 rounded-lg hover:bg-gray-400 transition duration-300"
            >
              Cancel
            </button>
            <button
              onClick={handleSave}
              className="w-full bg-black text-white p-3 rounded-lg hover:bg-gray-800 transition duration-300"
            >
              Save Changes
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
