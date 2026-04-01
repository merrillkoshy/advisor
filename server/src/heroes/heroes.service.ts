import { Injectable } from '@nestjs/common';
import { PrismaService } from 'prisma/prisma.service';

@Injectable()
export class HeroesService {
  constructor(private readonly prisma: PrismaService) {}

  findAll() {
    return this.prisma.hero.findMany({
      include: {
        skills: {
          include: {
            effects: {
              include: {
                statKey: true,
              },
            },
          },
        },
      },
      orderBy: { name: 'asc' },
    });
  }

  findOne(id: number) {
    return this.prisma.hero.findUnique({
      where: { id },
      include: {
        skills: {
          include: {
            effects: {
              include: {
                statKey: true,
              },
            },
          },
        },
      },
    });
  }
}
