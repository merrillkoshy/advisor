// src/api/heroes.ts
import type { Hero } from "@/types";
import { queryOptions } from "@tanstack/react-query";
import { apiGet } from "./client";

export const heroesQuery = queryOptions({
  queryKey: ["heroes"],
  queryFn: () => apiGet<Hero[]>("/heroes"),
});

export const heroQuery = (heroId: number) =>
  queryOptions({
    queryKey: ["hero", heroId],
    queryFn: () => apiGet<Hero>(`/heroes/${heroId}`),
    enabled: !!heroId,
  });
