import { heroesQuery } from "@/api/heroes";
import { useQuery } from "@tanstack/react-query";

export function useHeroes() {
  return useQuery(heroesQuery);
}
