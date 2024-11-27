'use client';

import { useEffect } from 'react';
import Image from 'next/image';
import mailEnvelopeImage from '../images/check-mark.png'; 
import { useRouter } from 'next/navigation';
import Link from 'next/link';

export default function Confirmation() {
  const router = useRouter();

  useEffect(() => {
    const timer = setTimeout(() => {
      router.push('/signin'); 
    }, 3000); 

    return () => clearTimeout(timer); 
  }, [router]);

  return (
    <div className="flex flex-col items-center justify-center h-screen bg-[#ffffff]">
        <h1 className="text-2xl font-semibold text-black mb-4">Password Successfully Reset</h1>
      <Image src={mailEnvelopeImage} alt="Request sent" width={150} height={150} className="mb-2" />
      <p className="text-center text-gray-800 mb-4">
        Your password has been successfully reset 
      </p>
      <Link href="/signin" passHref>
        <button className="w-full bg-black text-white p-3 rounded-lg mt-4">
          Go to Sign In
        </button>
      </Link>
    </div>
  );
}
