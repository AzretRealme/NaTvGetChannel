package NaTV.Main.services.impl;


import NaTV.Main.dao.ChannelRepo;
import NaTV.Main.dao.DiscountRepo;
import NaTV.Main.dao.PriceRepo;
import NaTV.Main.models.dto.DiscountDto;
import NaTV.Main.models.dto.PriceDto;
import NaTV.Main.models.entity.Channel;
import NaTV.Main.models.objects.OutputChannel;
import NaTV.Main.models.objects.OutputDiscount;
import NaTV.Main.services.ChannelService;
import NaTV.Main.services.DiscountService;
import NaTV.Main.services.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChannelServiceImpl implements ChannelService {

    @Autowired
    private DiscountRepo discountRepo;
    @Autowired
    private PriceService priceService;
    @Autowired
    private DiscountService discountService;
    @Autowired
    private ChannelRepo channelRepo;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private PriceRepo priceRepo;


    @Override
    public List<Channel> tvChannels() {
        return channelRepo.findAll();

    }

    @Override
    public Channel saveTvChannel(Channel channel) {
        channel = channelRepo.save(channel);
        return channel;
    }
    

    @Override
    public List<OutputChannel> outputTvChannels(int pageLimit) {
        List<PriceDto> activeChannelPrice = priceService.allActiveChannelsPrices();
        List<PriceDto> activeChannelPriceFilterByActive = activeChannelPrice.stream()
                .filter(x -> x.getChannel().isActive())
                .collect(Collectors.toList());
        List<OutputChannel> outputChannels = new ArrayList<>();
        for (PriceDto p : activeChannelPriceFilterByActive) {
            OutputChannel outputTvChannelData = new OutputChannel();
            outputTvChannelData.setId(p.getChannel().getId());
            outputTvChannelData.setChannelName(p.getChannel().getChannelName());
            outputTvChannelData.setPhoto(p.getChannel().getPhoto());
            outputTvChannelData.setPrice(p.getPrice());
            List<DiscountDto> activeChannelDiscounts = discountService.allActiveChannelDiscounts(p.getChannel().getId());
            List<OutputDiscount> discountDataList = new ArrayList<>();
            for (DiscountDto d : activeChannelDiscounts) {
                OutputDiscount outputDiscount = new OutputDiscount();
                outputDiscount.setPercent(d.getPercent());
                outputDiscount.setMinDay(d.getMinDay());
                discountDataList.add(outputDiscount);
            }
            outputTvChannelData.setDiscounts(discountDataList);
            outputChannels.add(outputTvChannelData);
        }
        return outputChannels.stream().limit(pageLimit).collect(Collectors.toList());
    }



}
