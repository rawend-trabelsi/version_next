'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { FiEdit, FiTrash, FiUserPlus } from 'react-icons/fi';
import { HiOutlineLogout } from 'react-icons/hi';

export default function Dashboard() {
  const [user, setUser] = useState(null);
  const [profiles, setProfiles] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedRole, setSelectedRole] = useState('All');
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 7;
  const router = useRouter();

  useEffect(() => {
    if (typeof window !== 'undefined') {
      const loggedUser = JSON.parse(localStorage.getItem('user'));
      if (!loggedUser) {
        router.push('/signin');
      } else {
        setUser(loggedUser);
      }

      const storedProfiles = JSON.parse(localStorage.getItem('profiles')) || [];
      console.log('Loaded Profiles:', storedProfiles); // Vérification des profils
      setProfiles(storedProfiles);
    }
  }, [router]);

  const handleLogout = () => {
    localStorage.removeItem('user');
    router.push('/signin');
  };

  const handleDelete = (email) => {
    const updatedProfiles = profiles.filter((profile) => profile.email !== email);
    setProfiles(updatedProfiles);
    localStorage.setItem('profiles', JSON.stringify(updatedProfiles));
  };

  const handleEdit = (email) => {
    router.push(`/edit-profile?email=${email}`);
  };

  const filteredProfiles = profiles.filter((profile) => {
    const matchesSearchTerm =
      profile.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      profile.email.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesRole = selectedRole === 'All' || profile.role === selectedRole;
    return matchesSearchTerm && matchesRole;
  });

  const roles = ['All', ...new Set(profiles.map((profile) => profile.role))];

  // Pagination logic
  const totalPages = Math.ceil(filteredProfiles.length / itemsPerPage);
  const paginatedProfiles = filteredProfiles.slice(
    (currentPage - 1) * itemsPerPage,
    currentPage * itemsPerPage
  );

  const handlePageChange = (page) => {
    if (page >= 1 && page <= totalPages) {
      setCurrentPage(page);
    }
  };

  if (!user) {
    return null;
  }

  return (
    <div className="min-h-screen bg-gray-100 flex flex-col">
      <header className="bg-black text-white py-4 shadow-lg">
        <div className="container mx-auto flex justify-between items-center px-4">
          <h1 className="text-2xl font-bold">Dashboard</h1>
          <div className="flex items-center space-x-4">
            <span>Welcome, {user.email}</span>
            <button
              onClick={handleLogout}
              className="flex items-center bg-gray-800 px-3 py-2 rounded-md hover:bg-gray-700 transition"
            >
              <HiOutlineLogout className="mr-2" /> Logout
            </button>
          </div>
        </div>
      </header>

      <main className="container mx-auto p-6 flex flex-col space-y-8">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div className="bg-white p-6 rounded-lg shadow-md">
            <h2 className="text-lg font-semibold text-gray-700">Total Profiles</h2>
            <p className="text-3xl font-bold text-blue-600">{profiles.length}</p>
          </div>
          <div className="bg-white p-6 rounded-lg shadow-md">
            <h2 className="text-lg font-semibold text-gray-700">Active Profiles</h2>
            <p className="text-3xl font-bold text-green-600">
              {profiles.filter((profile) => profile.status === 'Active').length}
            </p>
          </div>
          <div className="bg-white p-6 rounded-lg shadow-md">
            <h2 className="text-lg font-semibold text-gray-700">Inactive Profiles</h2>
            <p className="text-3xl font-bold text-red-600">
              {profiles.filter((profile) => profile.status === 'Inactive').length}
            </p>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-md p-6">
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-xl font-bold text-gray-700">Profiles</h2>
            <Link href="/add-profile">
              <button className="flex items-center bg-black text-white px-4 py-2 rounded-md hover:bg-gray-800 transition">
                <FiUserPlus className="mr-2" /> Add Profile
              </button>
            </Link>
          </div>

          <div className="flex space-x-4 mb-4">
            <input
              type="text"
              placeholder="Search by name or email"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full p-2 border border-gray-300 rounded-md"
            />
            <select
              value={selectedRole}
              onChange={(e) => setSelectedRole(e.target.value)}
              className="p-2 border border-gray-300 rounded-md"
            >
              {roles.map((role) => (
                <option key={role} value={role}>
                  {role}
                </option>
              ))}
            </select>
          </div>

          <table className="w-full border-collapse border border-gray-300">
            <thead>
              <tr className="bg-gray-200 text-left">
                <th className="border border-gray-300 py-3 px-4">Name</th>
                <th className="border border-gray-300 py-3 px-4">Email</th>
                <th className="border border-gray-300 py-3 px-4">Role</th>
                <th className="border border-gray-300 py-3 px-4">Status</th>
                <th className="border border-gray-300 py-3 px-4 text-center">Actions</th>
              </tr>
            </thead>
            <tbody>
              {paginatedProfiles.length === 0 ? (
                <tr>
                  <td colSpan="5" className="py-4 px-4 text-center text-gray-500">
                    No profiles found.
                  </td>
                </tr>
              ) : (
                paginatedProfiles.map((profile) => (
                  <tr key={profile.email} className="border-t hover:bg-gray-100 transition">
                    <td className="border border-gray-300 py-3 px-4">{profile.name}</td>
                    <td className="border border-gray-300 py-3 px-4">{profile.email}</td>
                    <td className="border border-gray-300 py-3 px-4">{profile.role}</td>
                    <td className="border border-gray-300 py-3 px-4">{profile.status}</td>
                    <td className="border border-gray-300 py-3 px-4 text-center">
                      <div className="flex justify-center space-x-4">
                        <button
                          onClick={() => handleEdit(profile.email)}
                          className="text-blue-500 hover:underline flex items-center"
                        >
                          <FiEdit className="mr-1" /> Edit
                        </button>
                        <button
                          onClick={() => handleDelete(profile.email)}
                          className="text-red-500 hover:underline flex items-center"
                        >
                          <FiTrash className="mr-1" /> Delete
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>

        <div className="flex justify-center mt-4">
          <button
            onClick={() => handlePageChange(1)}
            disabled={currentPage === 1}
            className={`px-3 py-2 mx-1 rounded-md ${currentPage === 1 ? 'bg-gray-300 text-gray-500' : 'bg-black text-white'}`}
          >
            First
          </button>
          <button
            onClick={() => handlePageChange(currentPage - 1)}
            disabled={currentPage === 1}
            className={`px-3 py-2 mx-1 rounded-md ${currentPage === 1 ? 'bg-gray-300 text-gray-500' : 'bg-black text-white'}`}
          >
            Previous
          </button>
          {Array.from({ length: totalPages }, (_, i) => i + 1).map((page) => (
            <button
              key={page}
              onClick={() => handlePageChange(page)}
              className={`px-3 py-2 mx-1 rounded-md ${
                page === currentPage ? 'bg-blue-500 text-white' : 'bg-gray-200 text-black'
              }`}
            >
              {page}
            </button>
          ))}
          <button
            onClick={() => handlePageChange(currentPage + 1)}
            disabled={currentPage === totalPages}
            className={`px-3 py-2 mx-1 rounded-md ${
              currentPage === totalPages ? 'bg-gray-300 text-gray-500' : 'bg-black text-white'
            }`}
          >
            Next
          </button>
          <button
            onClick={() => handlePageChange(totalPages)}
            disabled={currentPage === totalPages}
            className={`px-3 py-2 mx-1 rounded-md ${
              currentPage === totalPages ? 'bg-gray-300 text-gray-500' : 'bg-black text-white'
            }`}
          >
            Last
          </button>
        </div>
      </main>
    </div>
  );
}
