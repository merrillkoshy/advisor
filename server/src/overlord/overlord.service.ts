import { Injectable } from '@nestjs/common';
import { PrismaService } from 'prisma/prisma.service';

@Injectable()
export class OverlordService {
  constructor(private readonly prisma: PrismaService) {}

  findAllClasses() {
    return this.prisma.overlordClass.findMany({
      orderBy: { level: 'asc' },
    });
  }
}
