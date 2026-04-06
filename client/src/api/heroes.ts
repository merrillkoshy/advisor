import { BACKEND_URL } from "@/constants";
import type { Hero } from "@/types";

export const getHeroes: () => Promise<Hero[]> = async () => {
  const response = await fetch(`${BACKEND_URL}/heroes`);
  return response.json();
};
