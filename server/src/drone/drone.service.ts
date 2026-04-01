import { Injectable } from '@nestjs/common';
import { PrismaService } from 'prisma/prisma.service';

@Injectable()
export class DroneService {
  constructor(private readonly prisma: PrismaService) {}

  findAll() {
    return this.prisma.droneComponent.findMany({
      include: {
        statKeys: {
          include: {
            statKey: true,
          },
        },
      },
      orderBy: { name: 'asc' },
    });
  }
}
