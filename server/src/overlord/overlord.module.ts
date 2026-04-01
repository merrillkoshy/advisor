import { Module } from '@nestjs/common';
import { OverlordController } from './overlord.controller';
import { OverlordService } from './overlord.service';

@Module({
  controllers: [OverlordController],
  providers: [OverlordService],
})
export class OverlordModule {}
