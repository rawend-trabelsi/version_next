'use client';

import { useEffect } from 'react';
import Image from 'next/image';
import mailEnvelopeImage from '../images/mail-envelope.png'; // Update the image import
import { useRouter } from 'next/navigation';

export default function Confirmation() {
  const router = useRouter();

  useEffect(() => {
    const timer = setTimeout(() => {
      router.push('/signin'); 
    }, 3000); 

    return () => clearTimeout(timer); 
  }, [router]);

  return (
    <div className="flex flex-col items-center justify-center h-screen bg-[#E1EFFF]">
      <Image src={mailEnvelopeImage} alt="Request sent" width={100} height={100} className="mb-4" />
      <h1 className="text-2xl font-semibold text-blue-600 mb-2">Request Sent!</h1>
      <p className="text-center text-gray-800 mb-4">
        Your sign-up request has been sent to the admin for approval. Please wait for confirmation.
      </p>
      <h3 className="text-gray-500">You will be redirected to the login page shortly.</h3>
    </div>
  );
}
