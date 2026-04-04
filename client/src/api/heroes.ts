import { BACKEND_URL } from "@/constants";

export const getHeroes = async () => {
  const response = await fetch(`${BACKEND_URL}/heroes`);
  return response;
};
// http://localhost:3000/heroes
// http://localhost:3000/drone-components
// http://localhost:3000/overlord/classes
